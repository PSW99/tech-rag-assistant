package com.techrag.tech_rag_assitant.embedding;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ChunkServiceTest {

    @Test
    @DisplayName("빈 텍스트는 빈 리스트 반환")
    void emptyText_returnsEmptyList() {
        // given
        ChunkService chunkService = new ChunkService(500, 50);

        // when
        List<String> chunks = chunkService.splitIntoChunks("");

        // then
        assertThat(chunks).isEmpty();
    }

    @Test
    @DisplayName("null 텍스트는 빈 리스트 반환")
    void nullText_returnsEmptyList() {
        // given
        ChunkService chunkService = new ChunkService(500, 50);

        // when
        List<String> chunks = chunkService.splitIntoChunks(null);

        // then
        assertThat(chunks).isEmpty();
    }

    @Test
    @DisplayName("청크 크기보다 작은 텍스트는 하나의 청크로 반환")
    void smallText_returnsSingleChunk() {
        // given
        ChunkService chunkService = new ChunkService(500, 50);
        String text = "Spring Boot는 자바 기반 웹 프레임워크입니다.";

        // when
        List<String> chunks = chunkService.splitIntoChunks(text);

        // then
        assertThat(chunks).hasSize(1);
        assertThat(chunks.get(0)).isEqualTo(text);
    }

    @Test
    @DisplayName("긴 텍스트는 여러 청크로 분할")
    void longText_splitsIntoMultipleChunks() {
        // given
        ChunkService chunkService = new ChunkService(100, 20);
        String text = "Spring Boot는 자바 기반의 웹 애플리케이션 프레임워크입니다. " +
                "간편한 설정과 빠른 개발이 가능합니다. " +
                "내장 톰캣을 제공하여 별도의 서버 설정이 필요없습니다. " +
                "자동 설정 기능으로 복잡한 XML 설정을 줄일 수 있습니다.";

        // when
        List<String> chunks = chunkService.splitIntoChunks(text);

        // then
        assertThat(chunks).hasSizeGreaterThan(1);
        chunks.forEach(chunk -> 
            assertThat(chunk.length()).isLessThanOrEqualTo(100)
        );
    }

    @Test
    @DisplayName("청크 간 오버랩이 적용됨")
    void chunks_haveOverlap() {
        // given
        ChunkService chunkService = new ChunkService(50, 10);
        String text = "가나다라마바사아자차카타파하 " +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ " +
                "0123456789 abcdefghijklmnop";

        // when
        List<String> chunks = chunkService.splitIntoChunks(text);

        // then
        assertThat(chunks).hasSizeGreaterThan(1);
        
        // 연속된 청크 사이에 겹치는 부분 확인
        for (int i = 0; i < chunks.size() - 1; i++) {
            String current = chunks.get(i);
            String next = chunks.get(i + 1);
            String endOfCurrent = current.substring(Math.max(0, current.length() - 10));
            
            // 오버랩 영역이 다음 청크에 포함되어 있는지 확인
            assertThat(next.contains(endOfCurrent.trim()) || 
                       endOfCurrent.contains(next.substring(0, Math.min(10, next.length())).trim()))
                    .isTrue();
        }
    }
}
