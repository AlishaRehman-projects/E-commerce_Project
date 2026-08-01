package com.example.ecom_proj.service;

import com.example.ecom_proj.model.*;
import com.example.ecom_proj.repo.CartRepo;
import com.example.ecom_proj.repo.OrderItemRepo;
import com.example.ecom_proj.repo.OrderRepo;
import com.example.ecom_proj.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
@Service
public class OrderService {
    @Autowired private OrderRepo orderRepo;
    @Autowired private OrderItemRepo orderItemRepo;
    @Autowired private CartService cartService;
    @Autowired private CartRepo cartRepo;
    @Autowired private UserRepo userRepo;
    public Order checkout(String email) {
        Users user = userRepo.findByEmail(email);
        Cart cart = cartService.getCart(email);

        if(cart.getItems().isEmpty()){
            throw new RuntimeException("Cart is Empty");
        }
        Order order = new Order();
        order.setUser(user);
        BigDecimal total = BigDecimal.ZERO;

        for(CartItem cartItem : cart.getItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setProductName(cartItem.getProduct().getName());
            orderItem.setPriceAtPurchase(cartItem.getProduct().getPrice());
            orderItem.setQuantity(cartItem.getQuantity());

            order.getItems().add(orderItem);
            total = total.add(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }
        order.setTotalAmount(total);
        order.setStatus("PENDING");
        Order savedOrder = orderRepo.save(order);

        // clear the cart after order is saved
        cartService.clearCart(email);
        return savedOrder;
    }
    public List<Order> getOrderHistory(String email) {
        Users user = userRepo.findByEmail(email);
        return orderRepo.findByUser(user);
    }

    public Order getOrderById(int orderId, String email) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new RuntimeException(("Order not found")));
        if (order.getUser().getEmail() == null || !order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }
        return order;
    }

    public Order updateStatus(int orderId, String status) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepo.save(order);
    }

}
