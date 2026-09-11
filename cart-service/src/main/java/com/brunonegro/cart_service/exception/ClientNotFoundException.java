package com.brunonegro.cart_service.exception;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(Long id) {
        super("Client not found with id " + id);
    }

    public ClientNotFoundException(String message) {
        super(message);
    }
}
