package com.example.documentApp.auth.dto;

/**
 * DTO for login response payload.
 */
public class AuthResponse {
    private String token;

    public AuthResponse() { }

    public AuthResponse(String token) {
        this.token = token;
    }

    // Getter and setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}