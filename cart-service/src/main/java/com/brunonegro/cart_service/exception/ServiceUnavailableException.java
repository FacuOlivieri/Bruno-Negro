package com.brunonegro.cart_service.exception;

public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(String serviceName) {
        super("El servicio " + serviceName + " no funciona en este momento, por favor, intentelo mas tarde");
    }
}
