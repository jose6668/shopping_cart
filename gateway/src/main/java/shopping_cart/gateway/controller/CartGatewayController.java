package shopping_cart.gateway.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
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
import shopping_cart.gateway.dto.AddCartItemRequestDTO;
import shopping_cart.gateway.dto.CreateCartRequestDTO;
import shopping_cart.gateway.dto.UpdateCartItemQuantityRequestDTO;
import shopping_cart.gateway.service.CartGatewayService;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
public class CartGatewayController {

    private final CartGatewayService cartGatewayService;

    @PostMapping
    public ResponseEntity<String> createCart(@Valid @RequestBody CreateCartRequestDTO request) {
        return cartGatewayService.createCart(request);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<String> getCartById(
        @PathVariable @Positive(message = "cartId must be a positive number") Long cartId
    ) {
        return cartGatewayService.getCartById(cartId);
    }

    @GetMapping("/{cartId}/total")
    public ResponseEntity<String> getCartTotal(
        @PathVariable @Positive(message = "cartId must be a positive number") Long cartId
    ) {
        return cartGatewayService.getCartTotal(cartId);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<String> addItemToCart(
        @PathVariable @Positive(message = "cartId must be a positive number") Long cartId,
        @Valid @RequestBody AddCartItemRequestDTO request
    ) {
        return cartGatewayService.addItemToCart(cartId, request);
    }

    @PutMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<String> updateCartItemQuantity(
        @PathVariable @Positive(message = "cartId must be a positive number") Long cartId,
        @PathVariable @Positive(message = "itemId must be a positive number") Long itemId,
        @Valid @RequestBody UpdateCartItemQuantityRequestDTO request
    ) {
        return cartGatewayService.updateCartItemQuantity(cartId, itemId, request);
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<String> deleteCartItem(
        @PathVariable @Positive(message = "cartId must be a positive number") Long cartId,
        @PathVariable @Positive(message = "itemId must be a positive number") Long itemId
    ) {
        return cartGatewayService.deleteCartItem(cartId, itemId);
    }
}
