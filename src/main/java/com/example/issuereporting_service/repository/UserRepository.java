package com.example.issuereporting_service.repository;

import com.example.issuereporting_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}