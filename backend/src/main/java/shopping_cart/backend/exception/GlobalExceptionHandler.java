package shopping_cart.backend.exception;

import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shopping_cart.backend.dto.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<String> details = exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(this::formatFieldError)
            .toList();

        return buildResponse(
            HttpStatus.BAD_REQUEST,
            "Solicitud invalida",
            "La peticion contiene campos invalidos",
            details
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException exception) {
        List<String> details = exception.getConstraintViolations()
            .stream()
            .map(violation -> violation.getMessage())
            .toList();

        return buildResponse(
            HttpStatus.BAD_REQUEST,
            "Solicitud invalida",
            "La peticion no cumple las restricciones definidas",
            details
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        return buildResponse(
            HttpStatus.CONFLICT,
            "Conflicto de datos",
            "No fue posible persistir el carrito con la informacion recibida",
            List.of(exception.getMostSpecificCause().getMessage())
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException exception) {
        return buildResponse(
            HttpStatus.NOT_FOUND,
            "Recurso no encontrado",
            exception.getMessage(),
            List.of(exception.getMessage())
        );
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessValidation(BusinessValidationException exception) {
        return buildResponse(
            HttpStatus.BAD_REQUEST,
            "Regla de negocio invalida",
            exception.getMessage(),
            List.of(exception.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(Exception exception) {
        return buildResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Error interno",
            "Ocurrio un error inesperado al procesar la solicitud",
            List.of(exception.getMessage())
        );
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
        HttpStatus status,
        String error,
        String message,
        List<String> details
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
            status.value(),
            error,
            message,
            details,
            LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(response);
    }

    private String formatFieldError(FieldError fieldError) {
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }
}
