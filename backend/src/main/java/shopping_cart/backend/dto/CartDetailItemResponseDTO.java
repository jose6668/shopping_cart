package shopping_cart.backend.dto;

import java.math.BigDecimal;

public record CartDetailItemResponseDTO(
    Long id,
    Long productId,
    String name,
    Integer quantity,
    BigDecimal price,
    BigDecimal subtotal
) {
}
