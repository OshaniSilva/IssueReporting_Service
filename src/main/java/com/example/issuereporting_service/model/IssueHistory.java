package com.example.issuereporting_service.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table (name = "issue_history")
public class IssueHistory implements Serializable {

    @Id
//    @Column (name = "id")
    public IssueHistoryIds issueHistoryIds;

    @Column (name = "issue_state")
    private String issueState;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "issue_id", nullable = false)
    public Issue issue;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IssueHistory that = (IssueHistory) o;
        return Objects.equals(issueHistoryIds, that.issueHistoryIds) && Objects.equals(issueState, that.issueState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueHistoryIds, issueState);
    }

    public IssueHistory() {
    }

    public IssueHistory(String issueState, Issue issue) {
        this.issueState = issueState;
        this.issue = issue;
    }

    public Issue getId() {
        return issue;
    }

    public void setId(Issue issue) {
        this.issue = issue;
    }

    public IssueHistoryIds getIssueHistoryIds() {
        return issueHistoryIds;
    }

    public void setIssueHistoryIds(IssueHistoryIds issueHistoryIds) {
        this.issueHistoryIds = issueHistoryIds;
    }

    public String getIssueState() {
        return issueState;
    }

    public void setIssueState(String issueState) {
        this.issueState = issueState;
    }

}
