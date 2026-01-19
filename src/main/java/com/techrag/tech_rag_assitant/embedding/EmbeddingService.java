package com.techrag.tech_rag_assitant.embedding;

import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final OpenAiService openAiService;

    @Value("${openai.model.embedding:text-embedding-ada-002}")
    private String embeddingModel;

    public List<Double> createEmbedding(String text) {
        log.debug("Creating embedding for text: {}...", 
                text.substring(0, Math.min(50, text.length())));

        EmbeddingRequest request = EmbeddingRequest.builder()
                .model(embeddingModel)
                .input(List.of(text))
                .build();

        EmbeddingResult result = openAiService.createEmbeddings(request);

        List<Double> embedding = result.getData().get(0).getEmbedding();
        log.debug("Embedding created, dimension: {}", embedding.size());

        return embedding;
    }

    public String embeddingToString(List<Double> embedding) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < embedding.size(); i++) {
            sb.append(embedding.get(i));
            if (i < embedding.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
