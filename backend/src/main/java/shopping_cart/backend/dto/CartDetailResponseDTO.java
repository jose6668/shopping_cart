package shopping_cart.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CartDetailResponseDTO(
    Long id,
    Long userId,
    String status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<CartDetailItemResponseDTO> items,
    BigDecimal total
) {
}
