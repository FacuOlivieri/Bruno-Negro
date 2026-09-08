package com.brunonegro.client_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

    private long idClient;
    private String firstName;
    private String surname;
    private String email;
    private String cellPhone;
    private String address;
}
