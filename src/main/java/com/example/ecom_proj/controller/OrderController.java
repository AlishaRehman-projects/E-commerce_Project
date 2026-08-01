package com.example.ecom_proj.controller;

import com.example.ecom_proj.model.Order;
import com.example.ecom_proj.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderService orderService;

    // place an order from current cart
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@AuthenticationPrincipal UserDetails userDetails){
        Order order = orderService.checkout(userDetails.getUsername());
        return ResponseEntity.ok(order);
    }

    // view all past Records
    @GetMapping
    public ResponseEntity<List<Order>> getOrderHistory(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orderService.getOrderHistory(userDetails.getUsername()));
    }

    // view a single past record
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@AuthenticationPrincipal UserDetails userDetails, @PathVariable int orderId){
        return ResponseEntity.ok(orderService.getOrderById(orderId, userDetails.getUsername()));

    }
    // admin: update status
    @PutMapping("{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStatus(@PathVariable int orderId, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateStatus(orderId, status));

    }
}
