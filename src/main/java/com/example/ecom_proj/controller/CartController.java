package com.example.ecom_proj.controller;

import com.example.ecom_proj.model.Cart;
import com.example.ecom_proj.model.Product;
import com.example.ecom_proj.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getCart(@AuthenticationPrincipal UserDetails userDetails){
        // check if it is getUserNAme not getEmail
        return ResponseEntity.ok(cartService.getCart(userDetails.getUsername()));
    }
    @PostMapping("/add/{productId}")
    public ResponseEntity<Cart> addToCart(@AuthenticationPrincipal UserDetails userDetails, @PathVariable int productId, @RequestParam(defaultValue = "1") int quantity){
        return ResponseEntity.ok(cartService.addToCart(userDetails.getUsername(), productId, quantity));
    }

    @DeleteMapping("remove/{cartItemId}")
    public ResponseEntity<Cart> removeFromCart(@AuthenticationPrincipal UserDetails userDetails, @PathVariable int cartItemId){
        return ResponseEntity.ok(cartService.removeFromCart(userDetails.getUsername(), cartItemId));
    }
    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<Cart> updateQuantity(@AuthenticationPrincipal UserDetails userDetails, @PathVariable int cartItemId,@RequestParam int quantity){
        return ResponseEntity.ok(cartService.updateQuantity(userDetails.getUsername(),cartItemId, quantity));
    }
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(@AuthenticationPrincipal UserDetails userDetails){
        cartService.clearCart(userDetails.getUsername());
        return ResponseEntity.ok("Cart Cleared");
    }
}
