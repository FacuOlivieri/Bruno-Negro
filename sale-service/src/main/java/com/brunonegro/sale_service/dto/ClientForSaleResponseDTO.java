package com.brunonegro.sale_service.dto;

import lombok.*;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter
@Setter
public class ClientForSaleResponseDTO {

    private String firstName;
    private String surname;
    private String cellPhone;
    private String address;

}
