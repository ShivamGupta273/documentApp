package com.example.documentApp.controller;

import com.example.documentApp.entity.Document;
import com.example.documentApp.entity.User;
import com.example.documentApp.entity.UserSession;
import com.example.documentApp.repository.UserRepository;
import com.example.documentApp.repository.UserSessionRepository;
import com.example.documentApp.service.DocumentService;

import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
	
	@Autowired
    private UserRepository userRepo;

    @Autowired
    private UserSessionRepository sessionRepo;

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }
    
 // Utility: Role Check
    private boolean hasRole(String token, String requiredRole) {
        UserSession session = sessionRepo.findByToken(token);
        if (session == null) return false;

        User user = userRepo.findById(session.getUserId()).orElse(null);
        if (user == null) return false;

        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(requiredRole));
    }

    private boolean hasAnyRole(String token, List<String> roles) {
        UserSession session = sessionRepo.findByToken(token);
        if (session == null) return false;

        User user = userRepo.findById(session.getUserId()).orElse(null);
        if (user == null) return false;

        return user.getRoles().stream()
                .anyMatch(role -> roles.contains(role.getName().toUpperCase()));
    }

    // Upload document endpoint
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadDocument(@Parameter(description = "File to upload", required = true)
    @RequestParam("file") MultipartFile file,
    @RequestHeader(value = "Authorization") String token) {
    	 if (!hasAnyRole(token, Arrays.asList("ADMIN", "EDITOR"))) {
             throw new RuntimeException("Unauthorized to upload document");
         }
    	try {
            Document savedDoc = documentService.ingestDocument(file);
            return ResponseEntity.ok(savedDoc);
            //return new ResponseEntity<>(savedDoc, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process document: " + e.getMessage());
        }
    }

    // Search documents by keyword
    @GetMapping("/search")
    public ResponseEntity<List<Document>> searchDocuments(@RequestParam(name = "id", required = false) Long id,
    		@RequestParam(name = "keyword", required = false) String keyword,
    		@RequestParam(name = "filename", required = false) String filename,
    		@RequestParam(name = "author", required = false) String author,
    		@RequestParam(name = "file_type", required = false) String file_type,
    		@RequestHeader(value = "Authorization") String token
    		){
    	List<Document> docs = new ArrayList<Document>();
        if (sessionRepo.findByToken(token) == null) {
            throw new RuntimeException("Invalid or expired session");
        }
        
        System.out.println("id: "+ id + " keyword: " + keyword + " filename: " + filename + " author: " + author + " file_type: "+ file_type);
        if (id == null && keyword == null && filename == null && author == null && file_type == null) {
        	docs = documentService.getAllDocuments();
        }else {
        	docs = documentService.searchDocuments(id, keyword, filename, author, file_type);
        }
        if (docs == null) {
            throw new RuntimeException("Document not found");
        }
        return ResponseEntity.ok(docs);
    }

    // Get all documents
    @GetMapping("/all")
    public ResponseEntity<List<Document>> getAllDocuments(@RequestHeader(value = "Authorization") String token) {
        List<Document> docs = documentService.getAllDocuments();
        return ResponseEntity.ok(docs);
    }
    
 
}
