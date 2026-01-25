package com.techrag.tech_rag_assitant.domain.document;

import com.techrag.tech_rag_assitant.domain.chunk.ChunkRepository;
import com.techrag.tech_rag_assitant.domain.document.dto.DocumentResponse;
import com.techrag.tech_rag_assitant.domain.document.dto.DocumentSaveRequest;
import com.techrag.tech_rag_assitant.embedding.ChunkService;
import com.techrag.tech_rag_assitant.embedding.EmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final ChunkRepository chunkRepository;
    private final ChunkService chunkService;
    private final EmbeddingService embeddingService;

    @Transactional
    public DocumentResponse save(DocumentSaveRequest request) {
        // 1. Document 저장
        Document document = Document.builder()
                .url(request.getUrl())
                .title(request.getTitle())
                .originalText(request.getText())
                .build();

        Document saved = documentRepository.save(document);
        log.info("Document saved: id={}, title={}", saved.getId(), saved.getTitle());

        // 2. 텍스트를 청크로 분할
        List<String> chunks = chunkService.splitIntoChunks(request.getText());
        log.info("Text split into {} chunks", chunks.size());

        // 3. 각 청크에 대해 임베딩 생성 및 저장 (Native Query 사용)
        for (String chunkText : chunks) {
            List<Float> embedding = embeddingService.createEmbedding(chunkText);
            String embeddingStr = embeddingService.embeddingToString(embedding);

            chunkRepository.saveWithEmbedding(saved.getId(), chunkText, embeddingStr);
            log.debug("Chunk saved: documentId={}, contentLength={}",
                    saved.getId(), chunkText.length());
        }

        return new DocumentResponse(saved);
    }

    public List<DocumentResponse> findAll() {
        return documentRepository.findAll().stream()
                .map(DocumentResponse::new)
                .toList();
    }

    public DocumentResponse findById(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found: " + id));
        return new DocumentResponse(document);
    }
}
