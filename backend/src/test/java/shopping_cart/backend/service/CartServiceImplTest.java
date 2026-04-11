package shopping_cart.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shopping_cart.backend.dto.AddCartItemRequestDTO;
import shopping_cart.backend.dto.CartDetailResponseDTO;
import shopping_cart.backend.dto.CartItemResponseDTO;
import shopping_cart.backend.entity.Cart;
import shopping_cart.backend.entity.CartItem;
import shopping_cart.backend.entity.CartStatus;
import shopping_cart.backend.exception.BusinessValidationException;
import shopping_cart.backend.exception.ResourceNotFoundException;
import shopping_cart.backend.repository.CartItemRepository;
import shopping_cart.backend.repository.CartRepository;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository repository;

    @Mock
    private CartItemRepository cartItemRepository;

    private CartServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CartServiceImpl(repository, cartItemRepository);
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

    @Test
    void shouldCreateCartItemWhenProductIsNotAlreadyInCart() {
        Cart cart = Cart.builder()
            .id(3L)
            .userId(10L)
            .status(CartStatus.ACTIVE)
            .createdAt(LocalDateTime.now().minusHours(1))
            .updatedAt(LocalDateTime.now().minusMinutes(10))
            .build();

        AddCartItemRequestDTO request = new AddCartItemRequestDTO(
            101L,
            "Mouse Logitech G203",
            2,
            new BigDecimal("85000")
        );

        CartItem savedItem = CartItem.builder()
            .id(12L)
            .cart(cart)
            .productId(101L)
            .name("Mouse Logitech G203")
            .quantity(2)
            .price(new BigDecimal("85000.00"))
            .subtotal(new BigDecimal("170000.00"))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        when(repository.findById(3L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findFirstByCartIdAndProductId(3L, 101L)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(savedItem);
        when(repository.save(any(Cart.class))).thenReturn(cart);

        CartItemResponseDTO response = service.addItemToCart(3L, request);

        assertEquals(12L, response.id());
        assertEquals(3L, response.cartId());
        assertEquals(101L, response.productId());
        assertEquals(2, response.quantity());
        assertEquals(new BigDecimal("170000.00"), response.subtotal());
        verify(cartItemRepository).save(any(CartItem.class));
        verify(repository).save(cart);
    }

    @Test
    void shouldIncreaseQuantityWhenProductAlreadyExistsInCart() {
        Cart cart = Cart.builder()
            .id(4L)
            .userId(18L)
            .status(CartStatus.ACTIVE)
            .createdAt(LocalDateTime.now().minusHours(2))
            .updatedAt(LocalDateTime.now().minusMinutes(20))
            .build();

        CartItem existingItem = CartItem.builder()
            .id(20L)
            .cart(cart)
            .productId(300L)
            .name("Keyboard")
            .quantity(1)
            .price(new BigDecimal("120000.00"))
            .subtotal(new BigDecimal("120000.00"))
            .createdAt(LocalDateTime.now().minusHours(1))
            .updatedAt(LocalDateTime.now().minusMinutes(5))
            .build();

        AddCartItemRequestDTO request = new AddCartItemRequestDTO(
            300L,
            "Keyboard",
            2,
            new BigDecimal("120000.00")
        );

        when(repository.findById(4L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findFirstByCartIdAndProductId(4L, 300L)).thenReturn(Optional.of(existingItem));
        when(cartItemRepository.save(existingItem)).thenReturn(existingItem);
        when(repository.save(any(Cart.class))).thenReturn(cart);

        CartItemResponseDTO response = service.addItemToCart(4L, request);

        assertEquals(3, response.quantity());
        assertEquals(new BigDecimal("360000.00"), response.subtotal());
    }

    @Test
    void shouldFailWhenCartDoesNotExist() {
        AddCartItemRequestDTO request = new AddCartItemRequestDTO(
            99L,
            "Product",
            1,
            new BigDecimal("10000")
        );

        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.addItemToCart(999L, request));
    }

    @Test
    void shouldFailWhenCartIsNotActive() {
        Cart cart = Cart.builder()
            .id(5L)
            .userId(25L)
            .status(CartStatus.CHECKED_OUT)
            .createdAt(LocalDateTime.now().minusHours(3))
            .updatedAt(LocalDateTime.now().minusHours(1))
            .build();

        AddCartItemRequestDTO request = new AddCartItemRequestDTO(
            10L,
            "Headset",
            1,
            new BigDecimal("50000")
        );

        when(repository.findById(5L)).thenReturn(Optional.of(cart));

        assertThrows(BusinessValidationException.class, () -> service.addItemToCart(5L, request));
    }

    @Test
    void shouldReturnCartDetailsWithItemsAndTotal() {
        Cart cart = Cart.builder()
            .id(8L)
            .userId(40L)
            .status(CartStatus.ACTIVE)
            .createdAt(LocalDateTime.now().minusHours(2))
            .updatedAt(LocalDateTime.now().minusMinutes(15))
            .build();

        CartItem firstItem = CartItem.builder()
            .id(1L)
            .cart(cart)
            .productId(101L)
            .name("Mouse Logitech G203")
            .quantity(2)
            .price(new BigDecimal("85000.00"))
            .subtotal(new BigDecimal("170000.00"))
            .createdAt(LocalDateTime.now().minusHours(1))
            .updatedAt(LocalDateTime.now().minusMinutes(40))
            .build();

        CartItem secondItem = CartItem.builder()
            .id(2L)
            .cart(cart)
            .productId(202L)
            .name("Teclado Redragon Kumara")
            .quantity(1)
            .price(new BigDecimal("190000.00"))
            .subtotal(new BigDecimal("190000.00"))
            .createdAt(LocalDateTime.now().minusMinutes(50))
            .updatedAt(LocalDateTime.now().minusMinutes(20))
            .build();

        when(repository.findById(8L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findAllByCartIdOrderByIdAsc(8L)).thenReturn(List.of(firstItem, secondItem));

        CartDetailResponseDTO response = service.getCartById(8L);

        assertEquals(8L, response.id());
        assertEquals(40L, response.userId());
        assertEquals(2, response.items().size());
        assertEquals(new BigDecimal("360000.00"), response.total());
    }

    @Test
    void shouldReturnCartDetailsWithEmptyItemsAndZeroTotal() {
        Cart cart = Cart.builder()
            .id(9L)
            .userId(50L)
            .status(CartStatus.ACTIVE)
            .createdAt(LocalDateTime.now().minusHours(1))
            .updatedAt(LocalDateTime.now().minusMinutes(5))
            .build();

        when(repository.findById(9L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findAllByCartIdOrderByIdAsc(9L)).thenReturn(List.of());

        CartDetailResponseDTO response = service.getCartById(9L);

        assertTrue(response.items().isEmpty());
        assertEquals(BigDecimal.ZERO, response.total());
    }

    @Test
    void shouldFailToGetCartDetailsWhenCartDoesNotExist() {
        when(repository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getCartById(404L));
    }
}
