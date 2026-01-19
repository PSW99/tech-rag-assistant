package com.techrag.tech_rag_assitant.embedding;

import com.theokanning.openai.embedding.Embedding;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import com.theokanning.openai.service.OpenAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmbeddingServiceTest {

    @Mock
    private OpenAiService openAiService;

    private EmbeddingService embeddingService;

    @BeforeEach
    void setUp() {
        embeddingService = new EmbeddingService(openAiService);
        ReflectionTestUtils.setField(embeddingService, "embeddingModel", "text-embedding-ada-002");
    }

    @Test
    @DisplayName("텍스트 임베딩 생성 성공")
    void createEmbedding_success() {
        // given
        String text = "Spring Boot는 자바 웹 프레임워크입니다.";
        List<Double> mockEmbedding = Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5);

        Embedding embedding = new Embedding();
        ReflectionTestUtils.setField(embedding, "embedding", mockEmbedding);

        EmbeddingResult result = new EmbeddingResult();
        ReflectionTestUtils.setField(result, "data", List.of(embedding));

        when(openAiService.createEmbeddings(any(EmbeddingRequest.class))).thenReturn(result);

        // when
        List<Double> actualEmbedding = embeddingService.createEmbedding(text);

        // then
        assertThat(actualEmbedding).hasSize(5);
        assertThat(actualEmbedding).containsExactly(0.1, 0.2, 0.3, 0.4, 0.5);
    }

    @Test
    @DisplayName("임베딩을 문자열로 변환")
    void embeddingToString_success() {
        // given
        List<Double> embedding = Arrays.asList(0.1, 0.2, 0.3);

        // when
        String result = embeddingService.embeddingToString(embedding);

        // then
        assertThat(result).isEqualTo("[0.1,0.2,0.3]");
    }

    @Test
    @DisplayName("빈 임베딩을 문자열로 변환")
    void embeddingToString_emptyList() {
        // given
        List<Double> embedding = List.of();

        // when
        String result = embeddingService.embeddingToString(embedding);

        // then
        assertThat(result).isEqualTo("[]");
    }
}
