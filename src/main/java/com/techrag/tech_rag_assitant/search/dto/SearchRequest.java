package com.techrag.tech_rag_assitant.search.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchRequest {

    @NotBlank(message = "질문을 입력해주세요")
    private String query;

    private int limit = 5;
}
