package com.techrag.tech_rag_assitant.llm;

import com.openai.client.OpenAIClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.techrag.tech_rag_assitant.search.dto.SearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LlmService {

    private final OpenAIClient openAIClient;

    @Value("${openai.model.chat:gpt-5-mini}")
    private String chatModel;

    public String generateAnswer(String question, List<SearchResult> contexts) {
        log.info("Generating answer for: {}", question);

        String systemPrompt = buildSystemPrompt(contexts);

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(chatModel)
                .addSystemMessage(systemPrompt)
                .addUserMessage(question)
                .maxCompletionTokens(1000L)
                .build();

        ChatCompletion completion = openAIClient.chat().completions().create(params);
        String answer = completion.choices().get(0).message().content().orElse("");

        log.info("Answer generated, length: {}", answer.length());
        return answer;
    }

    private String buildSystemPrompt(List<SearchResult> contexts) {
        StringBuilder sb = new StringBuilder();
        sb.append("당신은 사용자의 기술 질문에 답변하는 어시스턴트입니다.\n");
        sb.append("아래 제공된 문서 내용을 기반으로 질문에 답변해주세요.\n");
        sb.append("문서에 없는 내용은 '제공된 문서에서 해당 정보를 찾을 수 없습니다'라고 답변하세요.\n");
        sb.append("답변 마지막에 참고한 문서의 출처를 표시하세요.\n\n");
        sb.append("=== 참고 문서 ===\n\n");

        for (int i = 0; i < contexts.size(); i++) {
            SearchResult ctx = contexts.get(i);
            sb.append(String.format("[문서 %d]\n", i + 1));
            sb.append(String.format("제목: %s\n", ctx.getDocumentTitle()));
            sb.append(String.format("URL: %s\n", ctx.getDocumentUrl()));
            sb.append(String.format("내용: %s\n\n", ctx.getContent()));
        }

        return sb.toString();
    }
}
