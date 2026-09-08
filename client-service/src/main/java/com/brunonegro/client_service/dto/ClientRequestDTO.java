package com.brunonegro.client_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequestDTO {

    private String firstName;
    private String surname;
    private String email;
    private String password;
    private String cellPhone;
    private String address;
}
