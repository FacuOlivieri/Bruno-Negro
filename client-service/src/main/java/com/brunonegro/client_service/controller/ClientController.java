package com.brunonegro.client_service.controller;

import com.brunonegro.client_service.dto.ClientRequestDTO;
import com.brunonegro.client_service.dto.ClientDTO;
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

    @GetMapping("/find/email")
    @ResponseStatus(HttpStatus.OK)
    public Optional<ClientDTO> findByEmail(@RequestParam String email) {
        return clientService.findByEmail(email);
    }

    @GetMapping("/loginRequest")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public boolean loginRequest(@RequestBody LoginDTO login){
        return clientService.loginAutentification(login);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDTO create(@RequestBody ClientRequestDTO request) {
        return clientService.create(request);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClientDTO update(@PathVariable long id, @RequestBody ClientRequestDTO request) {
        return clientService.update(id, request);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        clientService.delete(id);
    }
}
