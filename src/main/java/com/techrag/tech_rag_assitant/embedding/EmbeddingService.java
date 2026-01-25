package com.techrag.tech_rag_assitant.embedding;

import com.openai.client.OpenAIClient;
import com.openai.models.embeddings.CreateEmbeddingResponse;
import com.openai.models.embeddings.EmbeddingCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final OpenAIClient openAIClient;

    @Value("${openai.model.embedding:text-embedding-3-small}")
    private String embeddingModel;

    public List<Float> createEmbedding(String text) {
        log.debug("Creating embedding for text: {}...",
                text.substring(0, Math.min(50, text.length())));

        EmbeddingCreateParams params = EmbeddingCreateParams.builder()
                .model(embeddingModel)
                .input(text)
                .build();

        CreateEmbeddingResponse response = openAIClient.embeddings().create(params);

        List<Float> embedding = response.data().get(0).embedding();
        log.debug("Embedding created, dimension: {}", embedding.size());

        return embedding;
    }

    public String embeddingToString(List<Float> embedding) {
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
