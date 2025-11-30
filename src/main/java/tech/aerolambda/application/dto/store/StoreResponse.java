package tech.aerolambda.application.dto.store;

import java.time.LocalDateTime;

public record StoreResponse(
        Long id,
        String name,
        String address,
        String phone,
        String email,
        int bookCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
