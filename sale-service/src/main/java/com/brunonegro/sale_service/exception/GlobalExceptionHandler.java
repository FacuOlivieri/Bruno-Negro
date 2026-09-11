package com.brunonegro.sale_service.exception;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //////////////////////////////////////////   404   //////////////////////////////////////////

    @ExceptionHandler(SaleNotFoundException.class)
    public ResponseEntity<ErrorHandler> handleSaleNotFound(SaleNotFoundException exception, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorHandler> handleCartNotFound(CartNotFoundException exception, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ErrorHandler> handleClientNotFound(ClientNotFoundException exception, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    //////////////////////////////////////////   502 / 500   //////////////////////////////////////////

    /*
     * Fallas remotas que el service no tradujo: cart-service o client-service caido,
     * timeout, o una respuesta con un status inesperado. El que llama no hizo nada mal,
     * asi que no puede recibir un 4xx.
     */
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorHandler> handleFeign(FeignException exception, HttpServletRequest request) {
        return build(HttpStatus.BAD_GATEWAY, "Downstream service is unavailable");
    }

    //Ultimo recurso: nada sale de este servicio fuera del contrato ErrorHandler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorHandler> handleUnexpected(Exception exception, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");
    }

    //////////////////////////////////////////   Builder Helper   //////////////////////////////////////////

    private ResponseEntity<ErrorHandler> build(HttpStatus status, String message) {
        ErrorHandler error = ErrorHandler.builder()
                .statusCode(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .build();

        return ResponseEntity.status(status).body(error);
    }
}