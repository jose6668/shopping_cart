package shopping_cart.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorResponse(
    int status,
    String error,
    String message,
    List<String> details,
    LocalDateTime timestamp
) {
}
