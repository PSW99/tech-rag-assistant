package com.techrag.tech_rag_assitant.search;

import com.techrag.tech_rag_assitant.search.dto.SearchRequest;
import com.techrag.tech_rag_assitant.search.dto.SearchResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping
    public ResponseEntity<List<SearchResult>> search(@Valid @RequestBody SearchRequest request) {
        List<SearchResult> results = searchService.search(request);
        return ResponseEntity.ok(results);
    }
}
