package com.example.issuereporting_service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.io.Serializable;
import java.util.List;

/* This model represents the User entity and it creates the user table in the db.
 *  Columns are also created with the given names */
@Entity
@Table(name = "user")
public class User implements Serializable {

    // @Id annotation is used to create the primary key which is the id of the user.
    @Id
    @Column(name = "user_id")
    private String userId;
//   @GeneratedValue(strategy = GenerationType.AUTO)

    @Column(name = "username")
    private String username;

    /*  Below the relationship between the user table and issue table is specified
     *  The user and issue tables are joined with a OneToMany relationship */
    @OneToMany(mappedBy = "testUser", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Issue> issues;

    // Default constructor of User
    public User() {
    }

    // This constructor is used for saving new users
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