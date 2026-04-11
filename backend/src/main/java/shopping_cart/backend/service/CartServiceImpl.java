package shopping_cart.backend.service;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping_cart.backend.dto.AddCartItemRequestDTO;
import shopping_cart.backend.dto.CartItemResponseDTO;
import shopping_cart.backend.dto.CartResponseDTO;
import shopping_cart.backend.entity.Cart;
import shopping_cart.backend.entity.CartItem;
import shopping_cart.backend.entity.CartStatus;
import shopping_cart.backend.exception.BusinessValidationException;
import shopping_cart.backend.exception.ResourceNotFoundException;
import shopping_cart.backend.repository.CartItemRepository;
import shopping_cart.backend.repository.CartRepository;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartCreationResult createCart(Long userId) {
        return cartRepository.findFirstByUserIdAndStatus(userId, CartStatus.ACTIVE)
            .map(existingCart -> new CartCreationResult(toResponse(existingCart), false))
            .orElseGet(() -> {
                Cart savedCart = cartRepository.save(Cart.builder()
                    .userId(userId)
                    .status(CartStatus.ACTIVE)
                    .build());

                return new CartCreationResult(toResponse(savedCart), true);
            });
    }

    @Override
    @Transactional
    public CartItemResponseDTO addItemToCart(Long cartId, AddCartItemRequestDTO request) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new ResourceNotFoundException("No existe un carrito con id " + cartId));

        if (cart.getStatus() != CartStatus.ACTIVE) {
            throw new BusinessValidationException("Solo se pueden agregar productos a carritos activos");
        }

        BigDecimal normalizedPrice = request.price().setScale(2, java.math.RoundingMode.HALF_UP);

        CartItem cartItem = cartItemRepository.findFirstByCartIdAndProductId(cartId, request.productId())
            .map(existingItem -> {
                existingItem.increaseQuantity(request.quantity());
                existingItem.updateProductSnapshot(request.name().trim(), normalizedPrice);
                return existingItem;
            })
            .orElseGet(() -> CartItem.builder()
                .cart(cart)
                .productId(request.productId())
                .name(request.name().trim())
                .quantity(request.quantity())
                .price(normalizedPrice)
                .build());

        CartItem savedItem = cartItemRepository.save(cartItem);
        cart.touch();
        cartRepository.save(cart);

        return toItemResponse(savedItem);
    }

    private CartResponseDTO toResponse(Cart cart) {
        return new CartResponseDTO(
            cart.getId(),
            cart.getUserId(),
            cart.getStatus().name(),
            cart.getCreatedAt(),
            cart.getUpdatedAt()
        );
    }

    private CartItemResponseDTO toItemResponse(CartItem cartItem) {
        return new CartItemResponseDTO(
            cartItem.getId(),
            cartItem.getCart().getId(),
            cartItem.getProductId(),
            cartItem.getName(),
            cartItem.getQuantity(),
            cartItem.getPrice(),
            cartItem.getSubtotal()
        );
    }
}
