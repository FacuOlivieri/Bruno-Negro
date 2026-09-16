package com.brunonegro.product_service.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(int id) {
        super("Category not found with id " + id);
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
