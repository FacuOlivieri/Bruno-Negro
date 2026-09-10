package com.brunonegro.sale_service.dto;

import lombok.*;

import java.util.List;

@Builder @Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CartDTO {

    private Long idCart;
    private Long idUser;
    private List<ProductDetailDTO> productList;
    private double total;

}
