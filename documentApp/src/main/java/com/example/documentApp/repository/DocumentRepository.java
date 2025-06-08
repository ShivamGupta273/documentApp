package com.example.documentApp.repository;

import com.example.documentApp.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    // Search documents by keyword contained in content (case insensitive)
    List<Document> findByContentContainingIgnoreCase(String keyword);
    
    @Query(value = "SELECT * FROM documents WHERE LOWER(content) LIKE LOWER(CONCAT('%', :keyword, '%'))"
    		+ "OR LOWER(filename) LIKE LOWER(CONCAT('%', :filename, '%'))"
    		+ "OR LOWER(author) LIKE LOWER(CONCAT('%', :author, '%'))"
    		+ "OR LOWER(file_type) LIKE LOWER(CONCAT('%', :file_type, '%'))", nativeQuery = true)
    List<Document> searchByKeyword(@Param("keyword") String keyword,
    		@Param("filename") String filename,
    		@Param("author") String author,
    		@Param("file_type") String file_type); 
}