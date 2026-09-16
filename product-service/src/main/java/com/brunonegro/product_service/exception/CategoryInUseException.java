package com.brunonegro.product_service.exception;

public class CategoryInUseException extends RuntimeException {

    public CategoryInUseException(int id) {
        super("La categoria " + id + " tiene productos asociados, reasignelos o eliminelos antes de borrarla");
    }
}
