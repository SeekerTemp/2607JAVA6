package com.vn.test.demob1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;

/**
 * Ghi chú - ánh xạ đúng bảng Notes của NotesDB.sql (đề phụ):
 * id INT IDENTITY(1,1), author NVARCHAR(100), content NVARCHAR(255),
 * created_at DATETIME NOT NULL, updated_at DATETIME NULL
 */
@Entity
@Table(name = "Notes")
public class Note {

    // id trong đề là INT nên dùng Integer, không dùng Long
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // @Nationalized: cột NVARCHAR, giữ được tiếng Việt có dấu
    @Nationalized
    @Column(nullable = false, length = 100)
    private String author;

    @Nationalized
    @Column(nullable = false, length = 255)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Note() {
    }

    public Note(String author, String content) {
        this.author = author;
        this.content = content;
    }

    // đề phụ #3: ghi chú mới luôn có created_at do server sinh
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
