package com.techrag.tech_rag_assitant.domain.document;

import com.techrag.tech_rag_assitant.domain.chunk.Chunk;
import com.techrag.tech_rag_assitant.domain.chunk.ChunkRepository;
import com.techrag.tech_rag_assitant.domain.document.dto.DocumentResponse;
import com.techrag.tech_rag_assitant.domain.document.dto.DocumentSaveRequest;
import com.techrag.tech_rag_assitant.embedding.ChunkService;
import com.techrag.tech_rag_assitant.embedding.EmbeddingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private ChunkRepository chunkRepository;

    @Mock
    private ChunkService chunkService;

    @Mock
    private EmbeddingService embeddingService;

    @InjectMocks
    private DocumentService documentService;

    @Test
    @DisplayName("문서 저장 시 청크 분할 및 임베딩 생성")
    void save_createsChunksWithEmbeddings() {
        // given
        DocumentSaveRequest request = new DocumentSaveRequest();
        ReflectionTestUtils.setField(request, "url", "https://example.com");
        ReflectionTestUtils.setField(request, "title", "테스트 문서");
        ReflectionTestUtils.setField(request, "text", "Spring Boot는 자바 웹 프레임워크입니다.");

        Document savedDocument = Document.builder()
                .url("https://example.com")
                .title("테스트 문서")
                .originalText("Spring Boot는 자바 웹 프레임워크입니다.")
                .build();
        ReflectionTestUtils.setField(savedDocument, "id", 1L);

        List<String> chunks = List.of("Spring Boot는 자바 웹 프레임워크입니다.");
        List<Float> embedding = Arrays.asList(0.1f, 0.2f, 0.3f);

        when(documentRepository.save(any(Document.class))).thenReturn(savedDocument);
        when(chunkService.splitIntoChunks(anyString())).thenReturn(chunks);
        when(embeddingService.createEmbedding(anyString())).thenReturn(embedding);
        when(embeddingService.embeddingToString(embedding)).thenReturn("[0.1,0.2,0.3]");
        when(chunkRepository.save(any(Chunk.class))).thenAnswer(i -> i.getArgument(0));

        // when
        DocumentResponse response = documentService.save(request);

        // then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("테스트 문서");

        verify(documentRepository).save(any(Document.class));
        verify(chunkService).splitIntoChunks("Spring Boot는 자바 웹 프레임워크입니다.");
        verify(embeddingService).createEmbedding("Spring Boot는 자바 웹 프레임워크입니다.");
        verify(chunkRepository).save(any(Chunk.class));
    }

    @Test
    @DisplayName("긴 문서는 여러 청크로 분할되어 저장")
    void save_multipleChunks() {
        // given
        DocumentSaveRequest request = new DocumentSaveRequest();
        ReflectionTestUtils.setField(request, "url", "https://example.com");
        ReflectionTestUtils.setField(request, "title", "긴 문서");
        ReflectionTestUtils.setField(request, "text", "아주 긴 텍스트...");

        Document savedDocument = Document.builder()
                .url("https://example.com")
                .title("긴 문서")
                .originalText("아주 긴 텍스트...")
                .build();
        ReflectionTestUtils.setField(savedDocument, "id", 1L);

        List<String> chunks = List.of("청크1", "청크2", "청크3");
        List<Float> embedding = Arrays.asList(0.1f, 0.2f, 0.3f);

        when(documentRepository.save(any(Document.class))).thenReturn(savedDocument);
        when(chunkService.splitIntoChunks(anyString())).thenReturn(chunks);
        when(embeddingService.createEmbedding(anyString())).thenReturn(embedding);
        when(embeddingService.embeddingToString(embedding)).thenReturn("[0.1,0.2,0.3]");
        when(chunkRepository.save(any(Chunk.class))).thenAnswer(i -> i.getArgument(0));

        // when
        documentService.save(request);

        // then
        verify(chunkRepository, times(3)).save(any(Chunk.class));
        verify(embeddingService, times(3)).createEmbedding(anyString());
    }
}
