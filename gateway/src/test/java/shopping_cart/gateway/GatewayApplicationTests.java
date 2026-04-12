package shopping_cart.gateway;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
class GatewayApplicationTests {

	@Test
	void applicationClassLoads() {
		assertNotNull(new GatewayApplication());
	}

}
