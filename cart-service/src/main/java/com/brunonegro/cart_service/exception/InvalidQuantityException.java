package com.brunonegro.cart_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidQuantityException extends RuntimeException {

    public InvalidQuantityException(int quantity) {
        super("La cantidad debe ser mayor a cero, se recibio " + quantity);
    }

    public InvalidQuantityException(String message) {
        super(message);
    }
}
