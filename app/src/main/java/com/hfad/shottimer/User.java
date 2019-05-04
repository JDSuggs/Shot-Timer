package com.hfad.shottimer;

import java.util.Date;

public class User {

//    private String first;
//    private String last;
    private String name;
    private String userId;
//    private Date createdTime;

    // No-argument constructor is required to support conversion of Firestore document to POJO
    public User() {}

    // All-argument constructor is required to support conversion of Firestore document to POJO
    public User(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }
}