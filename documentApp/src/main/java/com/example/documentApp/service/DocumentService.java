package com.example.documentApp.service;

import com.example.documentApp.entity.Document;
import com.example.documentApp.repository.DocumentRepository;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document ingestDocument(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        String fileSize = file.getSize()/1024 +"MB";
        String fileType;
        Date uploadedDate = new Date();
        if (filename == null) {
            throw new IllegalArgumentException("Filename must not be null");
        }

        String textContent;

        if (filename.toLowerCase().endsWith(".txt")) {
            // Read plain text
        	fileType = "txt";
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                textContent = sb.toString();
            }
        } else if (filename.toLowerCase().endsWith(".pdf")) {
            // Extract text from PDF
        	fileType = "pdf";
            try (PDDocument pdfDoc = PDDocument.load(file.getInputStream())) {
                PDFTextStripper stripper = new PDFTextStripper();
                textContent = stripper.getText(pdfDoc);
            }
        } else if (filename.toLowerCase().endsWith(".docx")) {
            // Extract text from DOCX
        	fileType = "docx";
            try (XWPFDocument docx = new XWPFDocument(file.getInputStream())) {
                XWPFWordExtractor extractor = new XWPFWordExtractor(docx);
                textContent = extractor.getText();
            }
        } else {
            throw new IllegalArgumentException("Unsupported file type. Supported: txt, pdf, docx");
        }

        Document document = new Document(filename, fileType, uploadedDate, fileSize, textContent);
        return documentRepository.save(document);
    }

    public List<Document> searchDocuments(String keyword, String filename, String author, String file_type) {
    	System.out.println("keyword: " + keyword + " filename: " + filename + " author: " + author + " file_type: "+ file_type);
    	return documentRepository.searchByKeyword(keyword, filename, author, file_type);
        //return documentRepository.findByContentContainingIgnoreCase(keyword);
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }
}
