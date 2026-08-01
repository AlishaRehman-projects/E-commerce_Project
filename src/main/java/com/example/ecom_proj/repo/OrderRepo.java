package com.example.ecom_proj.repo;

import com.example.ecom_proj.model.Order;
import com.example.ecom_proj.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {
    List<Order> findByUser(Users users);
}
