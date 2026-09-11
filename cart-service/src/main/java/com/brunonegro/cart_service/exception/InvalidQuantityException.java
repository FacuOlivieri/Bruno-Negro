package com.brunonegro.cart_service.exception;

public class InvalidQuantityException extends RuntimeException {

    public InvalidQuantityException(int quantity) {
        super("La cantidad debe ser mayor a cero, se recibio " + quantity);
    }

    public InvalidQuantityException(String message) {
        super(message);
    }
}
