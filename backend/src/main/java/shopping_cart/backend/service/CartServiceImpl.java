package shopping_cart.backend.service;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping_cart.backend.dto.AddCartItemRequestDTO;
import shopping_cart.backend.dto.CartDetailItemResponseDTO;
import shopping_cart.backend.dto.CartDetailResponseDTO;
import shopping_cart.backend.dto.CartItemResponseDTO;
import shopping_cart.backend.dto.CartResponseDTO;
import shopping_cart.backend.dto.CartTotalResponseDTO;
import shopping_cart.backend.dto.DeleteCartItemResponseDTO;
import shopping_cart.backend.dto.UpdateCartItemQuantityRequestDTO;
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

    @Override
    @Transactional
    public CartItemResponseDTO updateCartItemQuantity(Long cartId, Long itemId, UpdateCartItemQuantityRequestDTO request) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new ResourceNotFoundException("No existe un carrito con id " + cartId));

        if (cart.getStatus() != CartStatus.ACTIVE) {
            throw new BusinessValidationException("Solo se pueden actualizar productos en carritos activos");
        }

        CartItem cartItem = cartItemRepository.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("No existe un item de carrito con id " + itemId));

        if (!cart.getId().equals(cartItem.getCart().getId())) {
            throw new BusinessValidationException(
                "El item con id " + itemId + " no pertenece al carrito con id " + cartId
            );
        }

        cartItem.updateQuantity(request.quantity());

        CartItem savedItem = cartItemRepository.save(cartItem);
        cart.touch();
        cartRepository.save(cart);

        return toItemResponse(savedItem);
    }

    @Override
    @Transactional
    public DeleteCartItemResponseDTO deleteCartItem(Long cartId, Long itemId) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new ResourceNotFoundException("No existe un carrito con id " + cartId));

        if (cart.getStatus() != CartStatus.ACTIVE) {
            throw new BusinessValidationException("Solo se pueden eliminar productos de carritos activos");
        }

        CartItem cartItem = cartItemRepository.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("No existe un item de carrito con id " + itemId));

        if (!cart.getId().equals(cartItem.getCart().getId())) {
            throw new BusinessValidationException(
                "El item con id " + itemId + " no pertenece al carrito con id " + cartId
            );
        }

        cartItemRepository.delete(cartItem);
        cart.touch();
        cartRepository.save(cart);

        return new DeleteCartItemResponseDTO(
            "Item eliminado correctamente del carrito",
            cartId,
            itemId
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CartDetailResponseDTO getCartById(Long cartId) {
        Cart cart = getCartOrThrow(cartId);
        List<CartDetailItemResponseDTO> items = getCartItems(cartId);
        CartTotalResponseDTO cartTotal = buildCartTotal(cartId, items);

        return new CartDetailResponseDTO(
            cart.getId(),
            cart.getUserId(),
            cart.getStatus().name(),
            cart.getCreatedAt(),
            cart.getUpdatedAt(),
            items,
            cartTotal.totalAmount()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CartTotalResponseDTO getCartTotal(Long cartId) {
        getCartOrThrow(cartId);
        List<CartDetailItemResponseDTO> items = getCartItems(cartId);
        return buildCartTotal(cartId, items);
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

    private CartDetailItemResponseDTO toDetailItemResponse(CartItem cartItem) {
        return new CartDetailItemResponseDTO(
            cartItem.getId(),
            cartItem.getProductId(),
            cartItem.getName(),
            cartItem.getQuantity(),
            cartItem.getPrice(),
            cartItem.getSubtotal()
        );
    }

    private Cart getCartOrThrow(Long cartId) {
        return cartRepository.findById(cartId)
            .orElseThrow(() -> new ResourceNotFoundException("No existe un carrito con id " + cartId));
    }

    private List<CartDetailItemResponseDTO> getCartItems(Long cartId) {
        return cartItemRepository.findAllByCartIdOrderByIdAsc(cartId)
            .stream()
            .map(this::toDetailItemResponse)
            .toList();
    }

    private CartTotalResponseDTO buildCartTotal(Long cartId, List<CartDetailItemResponseDTO> items) {
        BigDecimal totalAmount = items.stream()
            .map(CartDetailItemResponseDTO::subtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItems = items.stream()
            .mapToInt(CartDetailItemResponseDTO::quantity)
            .sum();

        return new CartTotalResponseDTO(
            cartId,
            totalItems,
            totalAmount
        );
    }
}
