package com.example.issuereporting_service.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table (name = "issue_history")
public class IssueHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column (name = "id")
    long id;

    @Column (name = "created_time")
    private String createdTime;

    @Column (name = "issue_state")
    private String issueState;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "issue_id")
    public Issue issue;

    public IssueHistory() {
    }

    public IssueHistory(String issueState, String createdTime, Issue issue) {
        this.issueState = issueState;
        this.createdTime = createdTime;
        this.issue = issue;
    }

    public IssueHistory(String issueState, Issue issue) {
        this.issueState = issueState;
        this.issue = issue;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Issue getIssue() {
        return issue;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIssueState() {
        return issueState;
    }

    public void setIssueState(String issueState) {
        this.issueState = issueState;
    }

}
