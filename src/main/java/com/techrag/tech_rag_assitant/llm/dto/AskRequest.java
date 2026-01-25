package com.techrag.tech_rag_assitant.llm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AskRequest {

    @NotBlank(message = "질문을 입력해주세요")
    private String question;

    private int searchLimit = 5;
}
