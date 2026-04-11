package shopping_cart.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateCartItemQuantityRequestDTO(
    @NotNull(message = "quantity es obligatoria")
    @Positive(message = "quantity debe ser mayor a cero")
    Integer quantity
) {
}
