package com.example.documentApp.controller;

import com.example.documentApp.entity.Document;
import com.example.documentApp.service.DocumentService;

import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // Upload document endpoint
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadDocument(@Parameter(description = "File to upload", required = true)
    @RequestParam("file") MultipartFile file) {
        try {
            Document savedDoc = documentService.ingestDocument(file);
            return new ResponseEntity<>(savedDoc, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process document: " + e.getMessage());
        }
    }

    // Search documents by keyword
    @GetMapping("/search")
    public ResponseEntity<List<Document>> searchDocuments(@RequestParam("keyword") String keyword) {
        List<Document> docs = documentService.searchDocuments(keyword);
        return ResponseEntity.ok(docs);
    }

    // Get all documents
    @GetMapping("/all")
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> docs = documentService.getAllDocuments();
        return ResponseEntity.ok(docs);
    }
}
