package com.techrag.tech_rag_assitant.domain.chunk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChunkRepository extends JpaRepository<Chunk, Long> {

    @Query(value = """
        SELECT c.* FROM chunks c
        ORDER BY c.embedding <=> cast(:embedding as vector)
        LIMIT :limit
        """, nativeQuery = true)
    List<Chunk> findSimilarChunks(@Param("embedding") String embedding, @Param("limit") int limit);
}
