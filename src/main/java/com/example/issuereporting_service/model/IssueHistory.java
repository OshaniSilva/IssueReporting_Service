package com.example.issuereporting_service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import java.io.Serializable;

/* This model represents the IssueHistory entity and it creates the issue_history table in the db.
 *  Columns are also created with the given names */
@Entity
@Table(name = "issue_history")
public class IssueHistory implements Serializable {

    /* @Id annotation is used to create the primary key which is the id of the issue history.
     Id is an autogenerated value */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    long id;

    // Below are the other columns in the issue_history table
    @Column(name = "created_time")
    private String createdTime;
    @Column(name = "issue_state")
    private String issueState;

    /*  Below the relationship between the issue table and issue_history table is created
     *  The issue and issue_history tables are joined with a ManyToOne relationship
     *  The issueId is created as a foreign key in the issue_history table */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "issue_id")
    public Issue issue;

    // Default constructor of IssueHistory
    public IssueHistory() {
    }

    // This constructor is used for saving new issues history records
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
