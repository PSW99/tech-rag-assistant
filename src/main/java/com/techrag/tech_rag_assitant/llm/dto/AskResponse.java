package com.techrag.tech_rag_assitant.llm.dto;

import com.techrag.tech_rag_assitant.search.dto.SearchResult;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AskResponse {

    private String question;
    private String answer;
    private List<SearchResult> sources;
}
