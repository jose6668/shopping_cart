package shopping_cart.gateway.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import shopping_cart.gateway.dto.AddCartItemRequestDTO;
import shopping_cart.gateway.dto.CreateCartRequestDTO;
import shopping_cart.gateway.dto.UpdateCartItemQuantityRequestDTO;

@Service
@RequiredArgsConstructor
public class CartGatewayService {

    private final RestClient backendRestClient;

    public ResponseEntity<String> createCart(CreateCartRequestDTO request) {
        return backendRestClient.post()
            .uri("/api/v1/carts")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .exchange((clientRequest, clientResponse) -> mapBackendResponse(clientRequest, clientResponse));
    }

    public ResponseEntity<String> getCartById(Long cartId) {
        return backendRestClient.get()
            .uri("/api/v1/carts/{cartId}", cartId)
            .exchange((clientRequest, clientResponse) -> mapBackendResponse(clientRequest, clientResponse));
    }

    public ResponseEntity<String> getCartTotal(Long cartId) {
        return backendRestClient.get()
            .uri("/api/v1/carts/{cartId}/total", cartId)
            .exchange((clientRequest, clientResponse) -> mapBackendResponse(clientRequest, clientResponse));
    }

    public ResponseEntity<String> addItemToCart(Long cartId, AddCartItemRequestDTO request) {
        return backendRestClient.post()
            .uri("/api/v1/carts/{cartId}/items", cartId)
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .exchange((clientRequest, clientResponse) -> mapBackendResponse(clientRequest, clientResponse));
    }

    public ResponseEntity<String> updateCartItemQuantity(Long cartId, Long itemId, UpdateCartItemQuantityRequestDTO request) {
        return backendRestClient.put()
            .uri("/api/v1/carts/{cartId}/items/{itemId}", cartId, itemId)
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .exchange((clientRequest, clientResponse) -> mapBackendResponse(clientRequest, clientResponse));
    }

    public ResponseEntity<String> deleteCartItem(Long cartId, Long itemId) {
        return backendRestClient.delete()
            .uri("/api/v1/carts/{cartId}/items/{itemId}", cartId, itemId)
            .exchange((clientRequest, clientResponse) -> mapBackendResponse(clientRequest, clientResponse));
    }

    private ResponseEntity<String> mapBackendResponse(HttpRequest clientRequest, ClientHttpResponse clientResponse) {
        try {
            String responseBody = new String(
                clientResponse.getBody().readAllBytes(),
                StandardCharsets.UTF_8
            );

            HttpHeaders headers = new HttpHeaders();
            MediaType contentType = clientResponse.getHeaders().getContentType();
            if (contentType != null) {
                headers.setContentType(contentType);
            } else {
                headers.setContentType(MediaType.APPLICATION_JSON);
            }

            return ResponseEntity.status(clientResponse.getStatusCode())
                .headers(headers)
                .body(responseBody);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read backend response", exception);
        }
    }
}
