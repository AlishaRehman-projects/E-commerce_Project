package com.example.ecom_proj.repo;

import com.example.ecom_proj.model.Cart;
import com.example.ecom_proj.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<Cart, Integer> {
    Cart findByUser(Users user);
}
