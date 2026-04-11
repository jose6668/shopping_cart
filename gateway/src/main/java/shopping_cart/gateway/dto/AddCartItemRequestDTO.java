package shopping_cart.gateway.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record AddCartItemRequestDTO(
    @NotNull(message = "productId is required")
    @Positive(message = "productId must be a positive number")
    Long productId,

    @NotBlank(message = "name is required")
    String name,

    @NotNull(message = "quantity is required")
    @Positive(message = "quantity must be greater than zero")
    Integer quantity,

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "price must be greater than or equal to zero")
    BigDecimal price
) {
}
