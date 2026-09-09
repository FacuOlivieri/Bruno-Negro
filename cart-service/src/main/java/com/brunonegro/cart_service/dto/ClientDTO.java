package com.brunonegro.cart_service.dto;

import lombok.*;

@Builder @Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ClientDTO {

    private Long idClient;
    private String firstName;
    private String surname;
    private String email;
    private String password;
    private String cellPhone;
    private String address;

}
