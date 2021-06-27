package com.example.issuereporting_service.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @Column(name = "user_id")
    private String userId;
//    @GeneratedValue(strategy = GenerationType.AUTO)

    @Column(name = "username")
    private String username;

    @OneToMany(mappedBy = "testUser", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Issue> issues;

    public User() { }

    public User(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}