package com.example.documentApp.auth.service;

import com.example.documentApp.auth.dto.AuthRequest;
import com.example.documentApp.auth.dto.AuthResponse;
import com.example.documentApp.auth.dto.RegisterRequest;
import com.example.documentApp.auth.entity.Role;
import com.example.documentApp.auth.entity.User;
import com.example.documentApp.auth.repository.RoleRepository;
import com.example.documentApp.auth.repository.UserRepository;
import com.example.documentApp.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 * Service class for authentication logic: registration, login, and role assignment.
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Register new user with selected role
    public ResponseEntity<String> register(RegisterRequest request) {
        if (!userRepository.findByUsername(request.getUsername()).toString().isBlank()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        Role role = roleRepository.findByName(request.getRole());
        if (role == null) {
            return ResponseEntity.badRequest().body("Role does not exist: " + request.getRole());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Collections.singleton(role));

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    // Login and return JWT token
    public ResponseEntity<AuthResponse> login(AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            
            User user = (User) authentication.getPrincipal();
           // String token = jwtService.generateToken(user);
            String token = jwtUtil.generateToken(user.getUsername());
//            String token = jwtUtil.generateToken(request.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body(new AuthResponse("Invalid username or password"));
        }
    }
}