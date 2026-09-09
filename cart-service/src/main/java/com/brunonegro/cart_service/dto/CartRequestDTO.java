package com.brunonegro.cart_service.dto;

import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartRequestDTO {
    private Long idUser;
}