package com.example.ecom_proj.service;

import com.example.ecom_proj.model.Order;
import com.example.ecom_proj.repo.OrderRepo;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {
    @Value("${razorpay.key.id}")
    private String keyId;
    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Autowired
    private OrderRepo orderRepo;
    public JSONObject createOrder(int orderId) throws RazorpayException {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Order not Found"));
        if(!order.getStatus().equals("PENDING")) {
            throw new RuntimeException("Order is already "+order.getStatus());
        }
        RazorpayClient client = new RazorpayClient(keyId, keySecret);
        JSONObject options = new JSONObject();
        // Razorpay accepts amount in paise (1 INR = 100 paise)
        options.put("amount", order.getTotalAmount().multiply(new java.math.BigDecimal(100)).intValue());
        options.put("currency", "INR");
        options.put("receipt", "order_" + orderId);
        options.put("payment_capture", 1); // auto capture payment

        com.razorpay.Order razorpayOrder = client.orders.create(options);
        // save razorpay order id in our order
        order.setRazorpayOrderId(razorpayOrder.get("id"));
        orderRepo.save(order);

        JSONObject response = new JSONObject();
        response.put("razorpayOrderId", (String)razorpayOrder.get("id"));
        response.put("amount", (Integer)razorpayOrder.get("amount"));
        response.put("currency", (String)razorpayOrder.get("currency"));
        response.put("keyId", keyId);
        response.put("orderId", orderId);

        return response;
    }

    public Order verifyAndConfirmPayment(int orderId, String razorpayPaymentId, String razorpaySignature) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // verify Signature
        String razorpayOrderId = order.getRazorpayOrderId();
        String generatedSignature = generateSignature(razorpayOrderId, razorpayPaymentId);

        if(generatedSignature.equals(razorpaySignature)) {
            order.setStatus("PAID");
            order.setRazorpayPaymentId(razorpayPaymentId);
            return orderRepo.save(order);
        } else {
            throw new RuntimeException("Payment Verification failed");
        }
    }
    public String generateSignature(String razorpayOrderId, String razorpayPaymentId) {
        try {
            String data = razorpayOrderId + "|" + razorpayPaymentId;
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            javax.crypto.spec.SecretKeySpec secretKeySpec =
                    new javax.crypto.spec.SecretKeySpec(keySecret.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Signature generation failed", e);
        }
    }
}
