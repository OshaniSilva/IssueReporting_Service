package com.example.issuereporting_service.repository;

import com.example.issuereporting_service.model.IssueHistory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IssueHistoryRepository extends JpaRepository<IssueHistory, Long> {
}
