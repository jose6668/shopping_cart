package shopping_cart.gateway.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import shopping_cart.gateway.service.CartGatewayService;

@ExtendWith(MockitoExtension.class)
class CartGatewayControllerTest {

    @Mock
    private CartGatewayService cartGatewayService;

    private CartGatewayController controller;

    @BeforeEach
    void setUp() {
        controller = new CartGatewayController(cartGatewayService);
    }

    @Test
    void shouldDelegateCartTotalLookupToGatewayService() {
        ResponseEntity<String> expectedResponse = ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body("""
                {"cartId":5,"totalItems":4,"totalAmount":125000.00}
                """.trim());

        when(cartGatewayService.getCartTotal(5L)).thenReturn(expectedResponse);

        ResponseEntity<String> response = controller.getCartTotal(5L);

        assertEquals(expectedResponse, response);
        verify(cartGatewayService).getCartTotal(5L);
    }
}
