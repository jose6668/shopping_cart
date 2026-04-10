package shopping_cart.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shopping_cart.backend.dto.CartResponseDTO;
import shopping_cart.backend.entity.Cart;
import shopping_cart.backend.entity.CartStatus;
import shopping_cart.backend.repository.CartRepository;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    private final CartRepository cartRepository;

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

    private CartResponseDTO toResponse(Cart cart) {
        return new CartResponseDTO(
            cart.getId(),
            cart.getUserId(),
            cart.getStatus().name(),
            cart.getCreatedAt(),
            cart.getUpdatedAt()
        );
    }
}
