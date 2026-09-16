package com.brunonegro.product_service.exception;

public class CategoryAlreadyExistsException extends RuntimeException {

    public CategoryAlreadyExistsException(String name) {
        super("Ya existe una categoria con el nombre " + name);
    }
}
