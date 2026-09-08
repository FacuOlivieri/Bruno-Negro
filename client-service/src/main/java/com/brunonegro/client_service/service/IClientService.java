package com.brunonegro.client_service.service;

import com.brunonegro.client_service.dto.ClientDTO;
import com.brunonegro.client_service.dto.ClientRequestDTO;
import com.brunonegro.client_service.dto.LoginDTO;

import java.util.List;
import java.util.Optional;

public interface IClientService {

    List<ClientDTO> findAll();

    ClientDTO findById(long id);
    Optional<ClientDTO> findByEmail(String email);
    boolean loginAutentification(LoginDTO login);

    ClientDTO create(ClientRequestDTO request);

    ClientDTO update(long id, ClientRequestDTO request);

    void delete(long id);
}
