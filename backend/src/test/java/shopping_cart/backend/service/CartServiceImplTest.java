package shopping_cart.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shopping_cart.backend.entity.Cart;
import shopping_cart.backend.entity.CartStatus;
import shopping_cart.backend.repository.CartRepository;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository repository;

    private CartServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CartServiceImpl(repository);
    }

    @Test
    void shouldCreateCartWhenUserHasNoActiveCart() {
        Cart savedCart = Cart.builder()
            .id(1L)
            .userId(10L)
            .status(CartStatus.ACTIVE)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        when(repository.findFirstByUserIdAndStatus(10L, CartStatus.ACTIVE)).thenReturn(Optional.empty());
        when(repository.save(any(Cart.class))).thenReturn(savedCart);

        CartCreationResult result = service.createCart(10L);

        assertTrue(result.created());
        assertNotNull(result.cart().id());
        assertEquals(10L, result.cart().userId());
        assertEquals("ACTIVE", result.cart().status());
        verify(repository).save(any(Cart.class));
    }

    @Test
    void shouldReturnExistingCartWhenUserAlreadyHasActiveCart() {
        Cart existingCart = Cart.builder()
            .id(7L)
            .userId(15L)
            .status(CartStatus.ACTIVE)
            .createdAt(LocalDateTime.now().minusHours(1))
            .updatedAt(LocalDateTime.now().minusMinutes(30))
            .build();

        when(repository.findFirstByUserIdAndStatus(15L, CartStatus.ACTIVE)).thenReturn(Optional.of(existingCart));

        CartCreationResult result = service.createCart(15L);

        assertFalse(result.created());
        assertEquals(7L, result.cart().id());
        assertEquals(15L, result.cart().userId());
        assertEquals("ACTIVE", result.cart().status());
    }
}
