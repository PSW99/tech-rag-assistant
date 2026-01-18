package com.techrag.tech_rag_assitant.domain.document;

import com.techrag.tech_rag_assitant.domain.document.dto.DocumentResponse;
import com.techrag.tech_rag_assitant.domain.document.dto.DocumentSaveRequest;
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

    @Transactional
    public DocumentResponse save(DocumentSaveRequest request) {
        Document document = Document.builder()
                .url(request.getUrl())
                .title(request.getTitle())
                .originalText(request.getText())
                .build();

        Document saved = documentRepository.save(document);
        log.info("Document saved: id={}, title={}", saved.getId(), saved.getTitle());

        // TODO: 임베딩 작업을 Queue에 등록

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
