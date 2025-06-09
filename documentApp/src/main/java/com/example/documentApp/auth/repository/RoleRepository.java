package com.example.documentApp.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.documentApp.auth.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String role);
}