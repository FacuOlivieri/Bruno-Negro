package com.brunonegro.cart_service.exception;

public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException(Long id) {
        super("Cart not found with id " + id);
    }

    public CartNotFoundException(String message) {
        super(message);
    }
}