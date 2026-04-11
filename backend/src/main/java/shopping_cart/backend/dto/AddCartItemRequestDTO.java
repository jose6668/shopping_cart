package shopping_cart.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record AddCartItemRequestDTO(
    @NotNull(message = "productId es obligatorio")
    @Positive(message = "productId debe ser un valor positivo")
    Long productId,

    @NotBlank(message = "name es obligatorio")
    String name,

    @NotNull(message = "quantity es obligatoria")
    @Positive(message = "quantity debe ser mayor a cero")
    Integer quantity,

    @NotNull(message = "price es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "price debe ser mayor o igual a cero")
    BigDecimal price
) {
}
