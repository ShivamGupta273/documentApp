package com.example.documentApp.auth;

import com.example.documentApp.auth.dto.AuthRequest;
import com.example.documentApp.auth.dto.AuthResponse;
import com.example.documentApp.auth.dto.RegisterRequest;
import com.example.documentApp.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This controller provides endpoints for registering, logging in, and testing access control.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    // Login a user and return a JWT token
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest request) {
        return authService.login(request);
    }

    // Test endpoint to check if authorization works
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("You are authorized to access this endpoint.");
    }
}