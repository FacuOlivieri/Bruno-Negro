package com.brunonegro.cart_service.validator;

import com.brunonegro.cart_service.exception.ClientNotFoundException;
import com.brunonegro.cart_service.repository.IClientAPI;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
 * Validaciones del carrito que necesitan preguntarle a client-service.
 * OJO: esta clase hace I/O (llama por Feign), no valida solo con datos en memoria.
 */
@Component
public class ClientValidator {

    @Autowired
    private IClientAPI clientAPI;

    //Corta la operacion si el cliente no existe en client-service
    public void validateExists(Long idClient) {
        try {
            clientAPI.findById(idClient);
        } catch (FeignException.NotFound e) {
            throw new ClientNotFoundException(idClient);
        }
    }

}
