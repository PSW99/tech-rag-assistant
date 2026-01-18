package com.techrag.tech_rag_assitant.domain.chunk;

import com.techrag.tech_rag_assitant.domain.document.Document;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chunks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "vector(1536)")
    private String embedding;

    private LocalDateTime createdAt;

    @Builder
    public Chunk(Document document, String content, String embedding) {
        this.document = document;
        this.content = content;
        this.embedding = embedding;
        this.createdAt = LocalDateTime.now();
    }
}
