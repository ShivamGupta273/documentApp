package com.example.documentApp.auth.filter;

import com.example.documentApp.auth.service.CustomUserDetailsService;
import com.example.documentApp.auth.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    
    @Autowired
    public JwtFilter(@Lazy  JwtUtil jwtUtil,  CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // JWT validation logic here (optional: can leave empty for now)
        filterChain.doFilter(request, response);
    }
}