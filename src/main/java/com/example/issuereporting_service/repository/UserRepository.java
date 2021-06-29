package com.example.issuereporting_service.repository;

import com.example.issuereporting_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// By extending the JpaRepository we are able to perform all the CRUD operations
public interface UserRepository extends JpaRepository<User, Long> {
}