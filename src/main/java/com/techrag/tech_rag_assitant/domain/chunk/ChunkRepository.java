package com.techrag.tech_rag_assitant.domain.chunk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChunkRepository extends JpaRepository<Chunk, Long> {

    @Modifying
    @Query(value = """
        INSERT INTO chunks (document_id, content, embedding, created_at)
        VALUES (:documentId, :content, cast(:embedding as vector), NOW())
        """, nativeQuery = true)
    void saveWithEmbedding(@Param("documentId") Long documentId,
                           @Param("content") String content,
                           @Param("embedding") String embedding);

    @Query(value = """
        SELECT c.id, c.document_id, c.content, c.embedding, c.created_at,
               c.embedding <=> cast(:embedding as vector) AS distance
        FROM chunks c
        WHERE c.embedding IS NOT NULL
        ORDER BY c.embedding <=> cast(:embedding as vector)
        LIMIT :limit
        """, nativeQuery = true)
    List<Object[]> findSimilarChunksRaw(@Param("embedding") String embedding, @Param("limit") int limit);
}
