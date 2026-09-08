package com.brunonegro.client_service.service;

import com.brunonegro.client_service.dto.ClientDTO;
import com.brunonegro.client_service.dto.ClientRequestDTO;
import com.brunonegro.client_service.dto.LoginDTO;
import com.brunonegro.client_service.exception.ClientNotFoundException;
import com.brunonegro.client_service.mapper.ClientMapper;
import com.brunonegro.client_service.model.Client;
import com.brunonegro.client_service.repository.IClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService implements IClientService {

    @Autowired
    private IClientRepository clientRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> findAll() {
        return ClientMapper.toDtoList(clientRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDTO findById(long id) {
        return getClientOrThrow(id);
    }

    @Override
    public Optional<ClientDTO> findByEmail(String email) {
        return Optional.of(clientRepository.findByEmail(email)
                .map(ClientMapper::toDto)
                .orElseThrow(() -> new ClientNotFoundException("No existe cliente con ese email")));
    }

    @Override
    public boolean loginAutentification(LoginDTO login) {
        Optional<Client> clientFound = clientRepository.findByEmail(login.getEmail());
        return clientFound.isPresent() && clientFound.get().getPassword().equals(login.getPassword());
    }


    @Override
    @Transactional
    public ClientDTO create(ClientRequestDTO request) {
        Client saved = clientRepository.save(ClientMapper.toEntity(request));
        return ClientMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ClientDTO update(long id, ClientRequestDTO request) {
        Client client = findEntityOrThrow(id);
        client.setFirstName(request.getFirstName());
        client.setSurname(request.getSurname());
        client.setEmail(request.getEmail());
        client.setPassword(request.getPassword());
        client.setCellPhone(request.getCellPhone());
        client.setAddress(request.getAddress());
        return ClientMapper.toDto(clientRepository.save(client));
    }

    @Override
    @Transactional
    public void delete(long id) {
        if (!clientRepository.existsById(id)) {
            throw new ClientNotFoundException(id);
        }
        clientRepository.deleteById(id);
    }

    private ClientDTO getClientOrThrow(long id) {
        return ClientMapper.toDto(findEntityOrThrow(id));
    }

    private Client findEntityOrThrow(long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }
}
