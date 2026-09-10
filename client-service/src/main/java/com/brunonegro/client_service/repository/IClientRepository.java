package com.brunonegro.client_service.repository;

import com.brunonegro.client_service.dto.ClientForSaleResponseDTO;
import com.brunonegro.client_service.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);

    // Class-based projection: selecciona solo las 4 columnas del DTO, nunca el password
    Optional<ClientForSaleResponseDTO> findByIdClient(Long idClient);
}
