package com.hfad.shottimer;

public class User {

    private String name;
    private String userId;

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