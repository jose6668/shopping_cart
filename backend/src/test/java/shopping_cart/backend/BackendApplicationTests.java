package shopping_cart.backend;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class BackendApplicationTests {

	@Test
	void applicationClassLoads() {
		assertNotNull(new BackendApplication());
	}

}
