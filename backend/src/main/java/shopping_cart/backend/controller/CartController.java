package shopping_cart.backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping_cart.backend.dto.AddCartItemRequestDTO;
import shopping_cart.backend.dto.CartItemResponseDTO;
import shopping_cart.backend.dto.CartResponseDTO;
import shopping_cart.backend.dto.CreateCartRequestDTO;
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

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemResponseDTO> addItemToCart(
        @PathVariable @Positive(message = "cartId debe ser un valor positivo") Long cartId,
        @Valid @RequestBody AddCartItemRequestDTO request
    ) {
        CartItemResponseDTO response = cartService.addItemToCart(cartId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
