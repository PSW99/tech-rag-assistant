package com.techrag.tech_rag_assitant.embedding;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChunkService {

    private final int chunkSize;
    private final int overlap;

    public ChunkService(
            @Value("${chunk.size:500}") int chunkSize,
            @Value("${chunk.overlap:50}") int overlap) {
        this.chunkSize = chunkSize;
        this.overlap = overlap;
    }

    public List<String> splitIntoChunks(String text) {
        List<String> chunks = new ArrayList<>();

        if (text == null || text.isBlank()) {
            return chunks;
        }

        // 텍스트가 청크 크기보다 작으면 그대로 반환
        if (text.length() <= chunkSize) {
            chunks.add(text.trim());
            return chunks;
        }

        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + chunkSize, text.length());

            // 단어 중간에 끊기지 않도록 조정
            if (end < text.length()) {
                int lastSpace = text.lastIndexOf(' ', end);
                if (lastSpace > start) {
                    end = lastSpace;
                }
            }

            String chunk = text.substring(start, end).trim();
            if (!chunk.isEmpty()) {
                chunks.add(chunk);
            }

            // 다음 시작점 (오버랩 적용)
            start = end - overlap;
            if (start >= text.length() - overlap) {
                break;
            }
        }

        return chunks;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public int getOverlap() {
        return overlap;
    }
}
