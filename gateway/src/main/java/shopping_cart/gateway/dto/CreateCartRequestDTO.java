package shopping_cart.gateway.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCartRequestDTO(
    @NotNull(message = "userId is required")
    @Positive(message = "userId must be a positive number")
    Long userId
) {
}
