package com.brunonegro.cart_service.exception;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    ///////////////////////////////// 404 //////////////////////////////

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCartNotFound(CartNotFoundException e, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClientNotFound(ClientNotFoundException e, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException e, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    ///////////////////////////////// 400 //////////////////////////////

    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<ErrorResponse> handleInvalidQuantity(InvalidQuantityException e, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    ///////////////////////////////// 502 / 500 //////////////////////////////

    /*
     * Feign failures that the service layer did not translate: client-service or
     * product-service is down, timing out, or answering with an unexpected status.
     * It is not the caller's fault, so it must not surface as a 4xx.
     */
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeign(FeignException e, HttpServletRequest request) {
        return build(HttpStatus.BAD_GATEWAY, "Downstream service is unavailable", request);
    }

    //Last resort: nothing leaves this service outside the ErrorResponse contract
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception e, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", request);
    }

    ///////////////////////////////// Builder //////////////////////////////

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message, HttpServletRequest request) {
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(body);
    }
}