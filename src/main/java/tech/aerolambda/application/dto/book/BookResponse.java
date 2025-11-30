package tech.aerolambda.application.dto.book;

import tech.aerolambda.application.dto.author.AuthorResponse;
import tech.aerolambda.application.dto.store.StoreResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookResponse(
        Long id,
        String title,
        String isbn,
        BigDecimal price,
        String description,
        Integer publicationYear,
        AuthorSummary author,
        StoreSummary store,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public record AuthorSummary(Long id, String name) {}
    public record StoreSummary(Long id, String name) {}
}
