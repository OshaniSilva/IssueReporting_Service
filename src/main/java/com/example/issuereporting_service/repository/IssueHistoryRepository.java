package com.example.issuereporting_service.repository;

import com.example.issuereporting_service.model.IssueHistory;
import org.springframework.data.jpa.repository.JpaRepository;

// By extending the JpaRepository we are able to perform all the CRUD operations
public interface IssueHistoryRepository extends JpaRepository<IssueHistory, Long> {
}
