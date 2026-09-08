package com.brunonegro.client_service.mapper;

import com.brunonegro.client_service.dto.ClientDTO;
import com.brunonegro.client_service.dto.ClientRequestDTO;
import com.brunonegro.client_service.model.Client;

import java.util.List;

public final class ClientMapper {

    private ClientMapper() {
    }

    public static ClientDTO toDto(Client client) {
        if (client == null) {
            return null;
        }
        return ClientDTO.builder()
                .idClient(client.getIdClient())
                .firstName(client.getFirstName())
                .surname(client.getSurname())
                .email(client.getEmail())
                .cellPhone(client.getCellPhone())
                .address(client.getAddress())
                .build();
    }

    public static List<ClientDTO> toDtoList(List<Client> clients) {
        return clients.stream()
                .map(ClientMapper::toDto)
                .toList();
    }

    public static Client toEntity(ClientRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Client.builder()
                .firstName(dto.getFirstName())
                .surname(dto.getSurname())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .cellPhone(dto.getCellPhone())
                .address(dto.getAddress())
                .build();
    }
}
