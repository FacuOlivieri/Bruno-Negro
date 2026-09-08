package com.brunonegro.client_service.dto;

import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    private String email;
    private String password;

}
