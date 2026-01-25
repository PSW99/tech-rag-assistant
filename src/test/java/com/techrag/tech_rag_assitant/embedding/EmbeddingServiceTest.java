package com.techrag.tech_rag_assitant.embedding;

import com.openai.client.OpenAIClient;
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

@ExtendWith(MockitoExtension.class)
class EmbeddingServiceTest {

    @Mock
    private OpenAIClient openAIClient;

    private EmbeddingService embeddingService;

    @BeforeEach
    void setUp() {
        embeddingService = new EmbeddingService(openAIClient);
        ReflectionTestUtils.setField(embeddingService, "embeddingModel", "text-embedding-3-small");
    }

    @Test
    @DisplayName("임베딩을 문자열로 변환")
    void embeddingToString_success() {
        // given
        List<Float> embedding = Arrays.asList(0.1f, 0.2f, 0.3f);

        // when
        String result = embeddingService.embeddingToString(embedding);

        // then
        assertThat(result).isEqualTo("[0.1,0.2,0.3]");
    }

    @Test
    @DisplayName("빈 임베딩을 문자열로 변환")
    void embeddingToString_emptyList() {
        // given
        List<Float> embedding = List.of();

        // when
        String result = embeddingService.embeddingToString(embedding);

        // then
        assertThat(result).isEqualTo("[]");
    }
}
