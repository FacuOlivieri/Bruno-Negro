package com.brunonegro.cart_service.validator;

import com.brunonegro.cart_service.exception.ClientNotFoundException;
import com.brunonegro.cart_service.repository.IClientAPI;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ClientValidator {

    @Autowired
    private IClientAPI clientAPI;

    public void validateExists(Long idClient) {
        try {
            clientAPI.findById(idClient);
        } catch (FeignException.NotFound e) {
            throw new ClientNotFoundException(idClient);
        }
    }

}
