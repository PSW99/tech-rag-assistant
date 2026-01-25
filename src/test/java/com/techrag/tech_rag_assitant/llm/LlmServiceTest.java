package com.techrag.tech_rag_assitant.llm;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LlmServiceTest {

    @Mock
    private OpenAiService openAiService;

    private LlmService llmService;

    @BeforeEach
    void setUp() {
        llmService = new LlmService(openAiService);
        ReflectionTestUtils.setField(llmService, "chatModel", "gpt-3.5-turbo");
    }

    @Test
    @DisplayName("컨텍스트 기반 답변 생성")
    void generateAnswer_success() {
        // given
        String question = "Spring Boot 설정 방법은?";
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

        ChatMessage responseMessage = new ChatMessage("assistant", "Spring Boot는 application.yml 파일을 통해 설정할 수 있습니다.");
        ChatCompletionChoice choice = new ChatCompletionChoice();
        ReflectionTestUtils.setField(choice, "message", responseMessage);

        ChatCompletionResult result = new ChatCompletionResult();
        ReflectionTestUtils.setField(result, "choices", List.of(choice));

        when(openAiService.createChatCompletion(any(ChatCompletionRequest.class)))
                .thenReturn(result);

        // when
        String answer = llmService.generateAnswer(question, contexts);

        // then
        assertThat(answer).contains("application.yml");
    }

    @Test
    @DisplayName("빈 컨텍스트로 답변 생성")
    void generateAnswer_emptyContext() {
        // given
        String question = "알 수 없는 질문";
        List<SearchResult> contexts = List.of();

        ChatMessage responseMessage = new ChatMessage("assistant", "제공된 문서에서 해당 정보를 찾을 수 없습니다.");
        ChatCompletionChoice choice = new ChatCompletionChoice();
        ReflectionTestUtils.setField(choice, "message", responseMessage);

        ChatCompletionResult result = new ChatCompletionResult();
        ReflectionTestUtils.setField(result, "choices", List.of(choice));

        when(openAiService.createChatCompletion(any(ChatCompletionRequest.class)))
                .thenReturn(result);

        // when
        String answer = llmService.generateAnswer(question, contexts);

        // then
        assertThat(answer).isNotEmpty();
    }
}
