package shopping_cart.backend.dto;

import java.time.LocalDateTime;

public record CartResponseDTO(
    Long id,
    Long userId,
    String status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
