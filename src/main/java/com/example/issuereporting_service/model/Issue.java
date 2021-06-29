package com.example.issuereporting_service.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table (name = "issue")
public class Issue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "issue_id")
    private long id;

    @Column (name = "issue_type")
    private String issueType;
    @Column (name = "issue_description")
    private String issueDescription;
    @Column (name = "issue_state")
    private String issueState;
    @Column (name = "created_time")
    private String createdTime;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    public User testUser;

    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<IssueHistory> issueHistories;


    public Issue() {
    }

    public Issue( String issueType, String issueDescription, String issueState, String createdTime, User user) {
        this.issueType = issueType;
        this.issueDescription = issueDescription;
        this.issueState = issueState;
        this.createdTime = createdTime;
        this.testUser = user;
    }


    public Issue(int id, String issueType, String issueDescription, String issueState, String createdTime, User user) {
        this.id = id;
        this.issueType = issueType;
        this.issueDescription = issueDescription;
        this.issueState = issueState;
        this.createdTime = createdTime;
        this.testUser = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getIssueState() {
        return issueState;
    }

    public void setIssueState(String issueState) {
        this.issueState = issueState;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", issueType='" + issueType + '\'' +
                ", issueDescription='" + issueDescription + '\'' +
                ", issueState='" + issueState + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", testUser=" + testUser +
                '}';
    }
}
