package com.techrag.tech_rag_assitant.llm;

import com.techrag.tech_rag_assitant.llm.dto.AskRequest;
import com.techrag.tech_rag_assitant.llm.dto.AskResponse;
import com.techrag.tech_rag_assitant.search.SearchService;
import com.techrag.tech_rag_assitant.search.dto.SearchRequest;
import com.techrag.tech_rag_assitant.search.dto.SearchResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ask")
@RequiredArgsConstructor
public class AskController {

    private final SearchService searchService;
    private final LlmService llmService;

    @PostMapping
    public ResponseEntity<AskResponse> ask(@Valid @RequestBody AskRequest request) {
        // 1. 유사 문서 검색
        SearchRequest searchRequest = new SearchRequest();
        // SearchRequest에 값 설정을 위해 setter 또는 생성자 필요
        List<SearchResult> searchResults = searchService.searchByQuery(
                request.getQuestion(), request.getSearchLimit());

        // 2. LLM으로 답변 생성
        String answer = llmService.generateAnswer(request.getQuestion(), searchResults);

        // 3. 응답 반환
        AskResponse response = AskResponse.builder()
                .question(request.getQuestion())
                .answer(answer)
                .sources(searchResults)
                .build();

        return ResponseEntity.ok(response);
    }
}
