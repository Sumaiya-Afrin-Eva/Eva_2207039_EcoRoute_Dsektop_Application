package com.example.eco_route.service;

public class UserSessionManager {

    private static final UserSessionManager instance = new UserSessionManager();
    private String currentUserId;
    private String currentUserEmail;

    private UserSessionManager() {}

    public static UserSessionManager getInstance() {
        return instance;
    }

    public void setCurrentUserId(String userId) {
        this.currentUserId = userId;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
    }

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public void logout() {
        this.currentUserId = null;
        this.currentUserEmail = null;
    }
}