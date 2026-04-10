package shopping_cart.backend.service;

import shopping_cart.backend.dto.CartResponseDTO;

public record CartCreationResult(
    CartResponseDTO cart,
    boolean created
) {
}
