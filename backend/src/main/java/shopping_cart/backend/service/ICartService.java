package shopping_cart.backend.service;

import shopping_cart.backend.dto.AddCartItemRequestDTO;
import shopping_cart.backend.dto.CartDetailResponseDTO;
import shopping_cart.backend.dto.CartItemResponseDTO;

public interface ICartService {

    CartCreationResult createCart(Long userId);

    CartItemResponseDTO addItemToCart(Long cartId, AddCartItemRequestDTO request);

    CartDetailResponseDTO getCartById(Long cartId);
}
