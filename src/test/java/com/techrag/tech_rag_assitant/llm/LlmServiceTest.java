package com.techrag.tech_rag_assitant.llm;

import com.openai.client.OpenAIClient;
import com.techrag.tech_rag_assitant.search.dto.SearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class LlmServiceTest {

    @Mock
    private OpenAIClient openAIClient;

    private LlmService llmService;

    @BeforeEach
    void setUp() {
        llmService = new LlmService(openAIClient);
        ReflectionTestUtils.setField(llmService, "chatModel", "gpt-5-mini");
    }

    @Test
    @DisplayName("SearchResult가 정상적으로 생성됨")
    void searchResult_creation() {
        // given
        List<SearchResult> contexts = List.of(
                SearchResult.builder()
                        .chunkId(1L)
                        .content("Spring Boot는 application.yml로 설정합니다.")
                        .documentId(1L)
                        .documentTitle("Spring 가이드")
                        .documentUrl("https://example.com")
                        .distance(0.1)
                        .build()
        );

        // then
        assertThat(contexts).hasSize(1);
        assertThat(contexts.get(0).getDocumentTitle()).isEqualTo("Spring 가이드");
    }
}
