package com.techrag.tech_rag_assitant.domain.document.dto;

import com.techrag.tech_rag_assitant.domain.document.Document;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DocumentResponse {

    private final Long id;
    private final String url;
    private final String title;
    private final String text;
    private final LocalDateTime createdAt;

    public DocumentResponse(Document document) {
        this.id = document.getId();
        this.url = document.getUrl();
        this.title = document.getTitle();
        this.text = document.getOriginalText();
        this.createdAt = document.getCreatedAt();
    }
}
