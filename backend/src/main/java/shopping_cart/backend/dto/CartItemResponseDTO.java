package shopping_cart.backend.dto;

import java.math.BigDecimal;

public record CartItemResponseDTO(
    Long id,
    Long cartId,
    Long productId,
    String name,
    Integer quantity,
    BigDecimal price,
    BigDecimal subtotal
) {
}
