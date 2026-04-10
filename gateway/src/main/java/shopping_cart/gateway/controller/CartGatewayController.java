package shopping_cart.gateway.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping_cart.gateway.dto.CreateCartRequestDTO;
import shopping_cart.gateway.service.CartGatewayService;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CartGatewayController {

    private final CartGatewayService cartGatewayService;

    @PostMapping
    public ResponseEntity<String> createCart(@Valid @RequestBody CreateCartRequestDTO request) {
        return cartGatewayService.createCart(request);
    }
}
