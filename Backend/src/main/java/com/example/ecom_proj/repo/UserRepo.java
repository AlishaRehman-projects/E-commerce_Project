package com.example.ecom_proj.repo;

import com.example.ecom_proj.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {

    Users findByEmail(String email);
    Users findByUsername(String username);
}

