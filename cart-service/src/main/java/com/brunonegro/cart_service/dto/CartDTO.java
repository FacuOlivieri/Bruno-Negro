package com.brunonegro.cart_service.dto;

import com.brunonegro.cart_service.model.ProductDetail;
import lombok.*;

import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    private Long idCart;
    private Long idUser;
    private List<ProductDetailDTO> productList;
    private double total;
}