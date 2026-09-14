package com.brunonegro.client_service.controller;

import com.brunonegro.client_service.dto.ClientRequestDTO;
import com.brunonegro.client_service.dto.ClientDTO;
import com.brunonegro.client_service.dto.ClientForSaleResponseDTO;
import com.brunonegro.client_service.dto.LoginDTO;
import com.brunonegro.client_service.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;


    /////////////////////// Get ///////////////////////


    @GetMapping("/find/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ClientDTO> findAll() {
        return clientService.findAll();
    }

    @GetMapping("/find/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClientDTO findById(@PathVariable long id) {
        return clientService.findById(id);
    }

    @GetMapping("/find/{id}/for-sale")
    @ResponseStatus(HttpStatus.OK)
    public ClientForSaleResponseDTO findForSaleById(@PathVariable long id) {
        return clientService.findForSaleById(id);
    }

    @GetMapping("/find/email")
    @ResponseStatus(HttpStatus.OK)
    public Optional<ClientDTO> findByEmail(@RequestParam String email) {
        return clientService.findByEmail(email);
    }






    /////////////////////// Post ///////////////////////




    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDTO create(@RequestBody ClientRequestDTO request) {
        return clientService.create(request);
    }

    @PostMapping("/loginRequest")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public boolean loginRequest(@RequestBody LoginDTO login){
        return clientService.loginAutentification(login);
    }

    /////////////////////// Put ///////////////////////


    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClientDTO update(@PathVariable long id, @RequestBody ClientRequestDTO request) {
        return clientService.update(id, request);
    }



    /////////////////////// Delete ///////////////////////


    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        clientService.delete(id);
    }
}
