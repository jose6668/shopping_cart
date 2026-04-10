package shopping_cart.gateway.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import shopping_cart.gateway.dto.CreateCartRequestDTO;

@Service
@RequiredArgsConstructor
public class CartGatewayService {

    private final RestClient backendRestClient;

    public ResponseEntity<String> createCart(CreateCartRequestDTO request) {
        return backendRestClient.post()
            .uri("/api/v1/carts")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .exchange((clientRequest, clientResponse) -> {
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
            });
    }
}
