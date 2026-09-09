package com.brunonegro.cart_service.repository;

import com.brunonegro.cart_service.dto.ClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "client-service")
public interface IClientAPI {

    @GetMapping("/clients/find/{id}")
    ClientDTO findById(@PathVariable long id);


}
