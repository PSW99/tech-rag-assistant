package com.techrag.tech_rag_assitant.search.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchResult {

    private Long chunkId;
    private String content;
    private Long documentId;
    private String documentTitle;
    private String documentUrl;
    private Double distance;
}
