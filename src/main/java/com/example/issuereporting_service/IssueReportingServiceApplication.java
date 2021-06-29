package com.example.issuereporting_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@Bean(name="entityManagerFactory")
public class IssueReportingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(IssueReportingServiceApplication.class, args);
    }
}
