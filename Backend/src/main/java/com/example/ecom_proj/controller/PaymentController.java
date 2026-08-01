package com.example.ecom_proj.controller;

import com.example.ecom_proj.model.Order;
import com.example.ecom_proj.service.PaymentService;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    // STEP 1. create Razorpay order
    @PostMapping("/create/{orderId}")
    private ResponseEntity<?> createPayment(@PathVariable int orderId, @AuthenticationPrincipal UserDetails userDetails) throws RazorpayException {
        //JSONObject is used because the method is returning structured data in JSON format instead of a single value.
        JSONObject response = paymentService.createOrder(orderId);
        return ResponseEntity.ok(response.toMap());

    }

    // STEP 2.verify Payment
    @PostMapping("/verify/{orderId}")
    private ResponseEntity<?> verifyPayment(@PathVariable int orderId, @RequestParam String razorpayPaymentId,
                                            @RequestParam String razorpaySignature, @AuthenticationPrincipal UserDetails userDetails) {

        Order order = paymentService.verifyAndConfirmPayment(orderId, razorpayPaymentId, razorpaySignature);
        return ResponseEntity.ok(order);

    }
}
