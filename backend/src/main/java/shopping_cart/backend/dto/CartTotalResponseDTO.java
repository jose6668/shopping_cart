package shopping_cart.backend.dto;

import java.math.BigDecimal;

public record CartTotalResponseDTO(
    Long cartId,
    Integer totalItems,
    BigDecimal totalAmount
) {
}
