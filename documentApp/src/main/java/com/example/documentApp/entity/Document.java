package com.example.documentApp.entity;

import java.util.Date;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;
    
    @Column
    private String path;
    
    @Column
    private String author;
    
    @Column
    private String fileType;
    
    @Column
    private Date uploadedDate;
    
    @Column
    private String fileSize;

    @Column(columnDefinition = "TEXT")
    private String content;
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Date getUploadedDate() {
		return uploadedDate;
	}

	public void setUploadedDate(Date uploadedDate) {
		this.uploadedDate = uploadedDate;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Document() {
    }

    public Document(String filename, String fileType, Date uploadedDate, String fileSize, String content) {
        this.filename = filename;
//        this.path = path;
//        this.author = author;
        this.fileType = fileType;
        this.uploadedDate = uploadedDate;
        this.fileSize = fileSize;
        this.content = content;
    }
}
