package com.example.documentApp.repository;

import com.example.documentApp.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepository extends JpaRepository<UserSession, Integer> {
    UserSession findByToken(String token);
    void deleteByToken(String token);
}