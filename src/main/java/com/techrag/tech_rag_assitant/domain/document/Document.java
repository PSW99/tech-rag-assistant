package com.techrag.tech_rag_assitant.domain.document;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2048)
    private String url;

    @Column(length = 500)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String originalText;

    private LocalDateTime createdAt;

    @Builder
    public Document(String url, String title, String originalText) {
        this.url = url;
        this.title = title;
        this.originalText = originalText;
        this.createdAt = LocalDateTime.now();
    }
}
