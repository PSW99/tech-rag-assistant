package com.techrag.tech_rag_assitant.domain.document.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DocumentSaveRequest {

    private String url;
    private String title;

    @NotBlank(message = "텍스트는 필수입니다")
    private String text;
}
