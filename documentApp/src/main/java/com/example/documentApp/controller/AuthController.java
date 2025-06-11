package com.example.documentApp.controller;

import com.example.documentApp.entity.*;
import com.example.documentApp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserSessionRepository sessionRepo;

    // Login
    @PostMapping("/login")
    public Map<String, String> login(@RequestParam(name = "username", required = true) String username,
    		@RequestParam(name = "password", required = true) String password) {
//        String username = loginData.get("username");
//        String password = loginData.get("password");

        User user = userRepo.findByUsernameAndPassword(username, password);
        if (user == null) {
            throw new RuntimeException("Invalid username or password");
        }

        String token = UUID.randomUUID().toString();
        UserSession session = new UserSession();
        session.setUserId(user.getId());
        session.setToken(token);
        sessionRepo.save(session);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }

    // Logout
    @PostMapping("/logout")
    @Transactional
    public Map<String, String> logout(@RequestHeader(value = "Authorization", required = true) String token) {
    	if (token == null || token.isEmpty()) {
            throw new RuntimeException("Missing token");
        }
    	sessionRepo.deleteByToken(token);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return response;
    }
    
    
}