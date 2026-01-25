package com.techrag.tech_rag_assitant.search;

import com.techrag.tech_rag_assitant.domain.chunk.ChunkRepository;
import com.techrag.tech_rag_assitant.domain.document.Document;
import com.techrag.tech_rag_assitant.domain.document.DocumentRepository;
import com.techrag.tech_rag_assitant.embedding.EmbeddingService;
import com.techrag.tech_rag_assitant.search.dto.SearchRequest;
import com.techrag.tech_rag_assitant.search.dto.SearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final ChunkRepository chunkRepository;
    private final DocumentRepository documentRepository;
    private final EmbeddingService embeddingService;

    public List<SearchResult> search(SearchRequest request) {
        return searchByQuery(request.getQuery(), request.getLimit());
    }

    public List<SearchResult> searchByQuery(String query, int limit) {
        log.info("Searching for: {}", query);

        // 1. 질문 임베딩 생성
        List<Double> queryEmbedding = embeddingService.createEmbedding(query);
        String embeddingStr = embeddingService.embeddingToString(queryEmbedding);

        // 2. 유사도 검색
        List<Object[]> rawResults = chunkRepository.findSimilarChunksRaw(embeddingStr, limit);

        // 3. 결과 변환
        List<SearchResult> results = new ArrayList<>();
        for (Object[] row : rawResults) {
            Long chunkId = ((Number) row[0]).longValue();
            Long documentId = ((Number) row[1]).longValue();
            String content = (String) row[2];
            Double distance = ((Number) row[5]).doubleValue();

            Document document = documentRepository.findById(documentId)
                    .orElse(null);

            SearchResult result = SearchResult.builder()
                    .chunkId(chunkId)
                    .content(content)
                    .documentId(documentId)
                    .documentTitle(document != null ? document.getTitle() : null)
                    .documentUrl(document != null ? document.getUrl() : null)
                    .distance(distance)
                    .build();

            results.add(result);
        }

        log.info("Found {} similar chunks", results.size());
        return results;
    }
}
