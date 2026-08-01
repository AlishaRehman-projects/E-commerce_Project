package com.example.ecom_proj.service;

import com.example.ecom_proj.model.Cart;
import com.example.ecom_proj.model.CartItem;
import com.example.ecom_proj.model.Product;
import com.example.ecom_proj.model.Users;
import com.example.ecom_proj.repo.CartItemRepo;
import com.example.ecom_proj.repo.CartRepo;
import com.example.ecom_proj.repo.ProductRepo;
import com.example.ecom_proj.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private UserRepo userRepo;

    public Cart getOrCreateCart(String email){
        Users user = userRepo.findByEmail(email);
        Cart cart = cartRepo.findByUser(user);
        if(cart == null){
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepo.save(cart);
        }
        return cart;
    }
    public Cart getCart(String email) {
        return getOrCreateCart(email);
    }

    public Cart addToCart(String email, int productId, int quantity) {
        Cart cart = getOrCreateCart(email);
        Product product = productRepo.findById(productId).orElseThrow(()->new RuntimeException("Product not found"));
        for(CartItem item : cart.getItems()){
            if(item.getProduct().getId() == productId){
                item.setQuantity(item.getQuantity()+quantity);
                cartItemRepo.save(item);
                return cartRepo.findById(cart.getId()).orElseThrow();
            }
        }
        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setProduct(product);
        newItem.setQuantity(quantity);
        cartItemRepo.save(newItem);

        return cartRepo.findById(cart.getId()).orElseThrow();
    }

    public Cart removeFromCart(String email, int cartItemId) {
        Cart cart = getOrCreateCart(email);
        CartItem item = cartItemRepo.findById(cartItemId).orElseThrow(()-> new RuntimeException("Item not found"));

        if(item.getCart().getId() != cart.getId()){
            throw new RuntimeException("Unauthorized");
        }
        cartItemRepo.delete(item);
        return cartRepo.findById(cart.getId()).orElseThrow();
    }

    public Cart updateQuantity(String email, int cartItemId, int quantity) {
        Cart cart = getOrCreateCart(email);
        CartItem item = cartItemRepo.findById(cartItemId).orElseThrow(()-> new RuntimeException("Item not found"));

        if(item.getCart().getId() != cart.getId()){
            throw new RuntimeException("Unauthorized");
        }

        if(quantity <= 0){
            cartItemRepo.delete(item);
        }
        else {
            item.setQuantity(quantity);
            cartItemRepo.save(item);
        }
        return cartRepo.findById(cart.getId()).orElseThrow();
    }

    public void clearCart(String email) {
        Cart cart = getOrCreateCart(email);
        cart.getItems().clear();
        cartRepo.save(cart);
    }
}
