package shopping_cart.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GatewayConfig {

    @Bean
    RestClient backendRestClient(
        RestClient.Builder builder,
        @Value("${services.backend.base-url}") String backendBaseUrl
    ) {
        return builder
            .baseUrl(backendBaseUrl)
            .build();
    }
}
