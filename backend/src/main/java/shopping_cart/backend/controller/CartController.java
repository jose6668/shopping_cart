package shopping_cart.backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping_cart.backend.dto.AddCartItemRequestDTO;
import shopping_cart.backend.dto.CartDetailResponseDTO;
import shopping_cart.backend.dto.CartItemResponseDTO;
import shopping_cart.backend.dto.CartResponseDTO;
import shopping_cart.backend.dto.CartTotalResponseDTO;
import shopping_cart.backend.dto.CreateCartRequestDTO;
import shopping_cart.backend.dto.DeleteCartItemResponseDTO;
import shopping_cart.backend.dto.UpdateCartItemQuantityRequestDTO;
import shopping_cart.backend.service.CartCreationResult;
import shopping_cart.backend.service.ICartService;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
public class CartController {

    private final ICartService cartService;

    @PostMapping
    public ResponseEntity<CartResponseDTO> createCart(@Valid @RequestBody CreateCartRequestDTO request) {
        CartCreationResult result = cartService.createCart(request.userId());
        HttpStatus status = result.created() ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(result.cart());
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDetailResponseDTO> getCartById(
        @PathVariable @Positive(message = "cartId debe ser un valor positivo") Long cartId
    ) {
        return ResponseEntity.ok(cartService.getCartById(cartId));
    }

    @GetMapping("/{cartId}/total")
    public ResponseEntity<CartTotalResponseDTO> getCartTotal(
        @PathVariable @Positive(message = "cartId debe ser un valor positivo") Long cartId
    ) {
        return ResponseEntity.ok(cartService.getCartTotal(cartId));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemResponseDTO> addItemToCart(
        @PathVariable @Positive(message = "cartId debe ser un valor positivo") Long cartId,
        @Valid @RequestBody AddCartItemRequestDTO request
    ) {
        CartItemResponseDTO response = cartService.addItemToCart(cartId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<CartItemResponseDTO> updateCartItemQuantity(
        @PathVariable @Positive(message = "cartId debe ser un valor positivo") Long cartId,
        @PathVariable @Positive(message = "itemId debe ser un valor positivo") Long itemId,
        @Valid @RequestBody UpdateCartItemQuantityRequestDTO request
    ) {
        return ResponseEntity.ok(cartService.updateCartItemQuantity(cartId, itemId, request));
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<DeleteCartItemResponseDTO> deleteCartItem(
        @PathVariable @Positive(message = "cartId debe ser un valor positivo") Long cartId,
        @PathVariable @Positive(message = "itemId debe ser un valor positivo") Long itemId
    ) {
        return ResponseEntity.ok(cartService.deleteCartItem(cartId, itemId));
    }
}
