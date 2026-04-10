package shopping_cart.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCartRequestDTO(
    @NotNull(message = "userId es obligatorio")
    @Positive(message = "userId debe ser un valor positivo")
    Long userId
) {
}
