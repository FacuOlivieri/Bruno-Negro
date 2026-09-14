package com.brunonegro.cart_service.validator;

import com.brunonegro.cart_service.exception.ClientNotFoundException;
import com.brunonegro.cart_service.exception.ServiceUnavailableException;
import com.brunonegro.cart_service.repository.IClientAPI;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ClientValidator {

    @Autowired
    private IClientAPI clientAPI;

    @CircuitBreaker(name="client-service")
    @Retry(name="client-service", fallbackMethod = "fallbackClientServiceUnavailable")
    public void validateExists(Long idClient) {
        try {
            clientAPI.findById(idClient);
        } catch (FeignException.NotFound e) {
            throw new ClientNotFoundException(idClient);
        }
    }

    public void  fallbackClientServiceUnavailable(Long idClient, FeignException exception) {
        throw new ServiceUnavailableException("Client-Service");
    }

}
