package com.example.issuereporting_service.repository;

import com.example.issuereporting_service.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {
}
