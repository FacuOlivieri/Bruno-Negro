package com.brunonegro.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    private Long idCart;
    private Long idUser;
    private List<CartProductDTO> productList;

    // TODO: integracion con product-service.
    // Agregar aca un campo "total" cuando se traigan los precios de product-service.
}