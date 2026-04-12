package shopping_cart.backend.dto;

public record DeleteCartItemResponseDTO(
    String message,
    Long cartId,
    Long itemId
) {
}
