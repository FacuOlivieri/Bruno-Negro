package com.brunonegro.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartProductDTO {

    private Long idCartProduct;
    private Long idProduct;
    private int quantity;

    // TODO: integracion con product-service.
    // Agregar aca los campos que vienen de product-service (productName, unitPrice,
    // subtotal) cuando este el cliente Feign. Ver CartMapper.toProductDto.
}
