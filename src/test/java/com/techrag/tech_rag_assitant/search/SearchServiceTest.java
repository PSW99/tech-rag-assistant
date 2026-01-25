package com.techrag.tech_rag_assitant.search;

import com.techrag.tech_rag_assitant.domain.chunk.ChunkRepository;
import com.techrag.tech_rag_assitant.domain.document.Document;
import com.techrag.tech_rag_assitant.domain.document.DocumentRepository;
import com.techrag.tech_rag_assitant.embedding.EmbeddingService;
import com.techrag.tech_rag_assitant.search.dto.SearchRequest;
import com.techrag.tech_rag_assitant.search.dto.SearchResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private ChunkRepository chunkRepository;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private EmbeddingService embeddingService;

    @InjectMocks
    private SearchService searchService;

    @Test
    @DisplayName("질문으로 유사한 청크 검색")
    void search_returnsSimilarChunks() {
        // given
        SearchRequest request = new SearchRequest();
        ReflectionTestUtils.setField(request, "query", "Spring Boot 설정 방법");
        ReflectionTestUtils.setField(request, "limit", 5);

        List<Float> embedding = Arrays.asList(0.1f, 0.2f, 0.3f);
        when(embeddingService.createEmbedding(anyString())).thenReturn(embedding);
        when(embeddingService.embeddingToString(embedding)).thenReturn("[0.1,0.2,0.3]");

        // Mock raw 결과: id, document_id, content, embedding, created_at, distance
        Object[] row = new Object[]{1L, 1L, "Spring Boot 설정 내용", "[0.1,0.2]", null, 0.15};
        List<Object[]> rawResults = new ArrayList<>();
        rawResults.add(row);
        when(chunkRepository.findSimilarChunksRaw(anyString(), anyInt()))
                .thenReturn(rawResults);

        Document document = Document.builder()
                .url("https://example.com")
                .title("Spring 가이드")
                .originalText("...")
                .build();
        ReflectionTestUtils.setField(document, "id", 1L);
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));

        // when
        List<SearchResult> results = searchService.search(request);

        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getContent()).isEqualTo("Spring Boot 설정 내용");
        assertThat(results.get(0).getDocumentTitle()).isEqualTo("Spring 가이드");
        assertThat(results.get(0).getDistance()).isEqualTo(0.15);
    }

    @Test
    @DisplayName("검색 결과가 없는 경우 빈 리스트 반환")
    void search_noResults_returnsEmptyList() {
        // given
        SearchRequest request = new SearchRequest();
        ReflectionTestUtils.setField(request, "query", "존재하지 않는 내용");
        ReflectionTestUtils.setField(request, "limit", 5);

        List<Float> embedding = Arrays.asList(0.1f, 0.2f, 0.3f);
        when(embeddingService.createEmbedding(anyString())).thenReturn(embedding);
        when(embeddingService.embeddingToString(embedding)).thenReturn("[0.1,0.2,0.3]");
        when(chunkRepository.findSimilarChunksRaw(anyString(), anyInt()))
                .thenReturn(new ArrayList<>());

        // when
        List<SearchResult> results = searchService.search(request);

        // then
        assertThat(results).isEmpty();
    }
}
