package com.example.documentApp.auth.repository;



import com.example.documentApp.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface to handle database operations for User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom method to find a user by username
    User findByUsername(String username);

    // Optional: Check if username already exists
    boolean existsByUsername(String username);
}