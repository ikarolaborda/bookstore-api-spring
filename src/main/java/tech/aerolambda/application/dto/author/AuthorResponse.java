package tech.aerolambda.application.dto.author;

import java.time.LocalDateTime;

public record AuthorResponse(
        Long id,
        String name,
        String bio,
        int bookCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
