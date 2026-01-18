package com.techrag.tech_rag_assitant.domain.document;

import com.techrag.tech_rag_assitant.domain.document.dto.DocumentResponse;
import com.techrag.tech_rag_assitant.domain.document.dto.DocumentSaveRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    public ResponseEntity<DocumentResponse> save(@Valid @RequestBody DocumentSaveRequest request) {
        DocumentResponse response = documentService.save(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DocumentResponse>> findAll() {
        return ResponseEntity.ok(documentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.findById(id));
    }
}
