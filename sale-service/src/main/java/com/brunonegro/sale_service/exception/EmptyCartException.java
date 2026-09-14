package com.brunonegro.sale_service.exception;

public class EmptyCartException extends RuntimeException {

    public EmptyCartException(Long idCart) {
        super("Cart " + idCart + " is empty, a sale needs at least one product");
    }

    public EmptyCartException(String message) {
        super(message);
    }
}