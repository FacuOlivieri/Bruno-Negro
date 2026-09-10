package com.brunonegro.client_service.dto;

public record ClientForSaleResponseDTO(
        String firstName,
        String surname,
        String cellPhone,
        String address) {
}