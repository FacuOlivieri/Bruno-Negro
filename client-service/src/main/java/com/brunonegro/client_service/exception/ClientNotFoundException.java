package com.brunonegro.client_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(long id) {
        super("Client not found with id " + id);
    }

    public ClientNotFoundException(String message) {
        super(message);
    }
}
