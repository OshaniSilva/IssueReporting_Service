package com.example.issuereporting_service.controller;

import com.example.issuereporting_service.model.Issue;
import com.example.issuereporting_service.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping ("/issue")
public class IssueController {

    @Autowired
    IssueRepository issueRepository;

    @PostMapping("/create")
    public String cretaeIssue (@RequestBody Issue issue) {
        return "test";
    }
}
