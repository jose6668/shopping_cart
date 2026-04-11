package shopping_cart.backend.service;

import shopping_cart.backend.dto.AddCartItemRequestDTO;
import shopping_cart.backend.dto.CartDetailResponseDTO;
import shopping_cart.backend.dto.CartItemResponseDTO;
import shopping_cart.backend.dto.CartTotalResponseDTO;
import shopping_cart.backend.dto.DeleteCartItemResponseDTO;
import shopping_cart.backend.dto.UpdateCartItemQuantityRequestDTO;

public interface ICartService {

    CartCreationResult createCart(Long userId);

    CartItemResponseDTO addItemToCart(Long cartId, AddCartItemRequestDTO request);

    CartItemResponseDTO updateCartItemQuantity(Long cartId, Long itemId, UpdateCartItemQuantityRequestDTO request);

    DeleteCartItemResponseDTO deleteCartItem(Long cartId, Long itemId);

    CartDetailResponseDTO getCartById(Long cartId);

    CartTotalResponseDTO getCartTotal(Long cartId);
}
