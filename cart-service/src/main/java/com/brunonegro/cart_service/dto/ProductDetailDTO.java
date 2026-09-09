package com.brunonegro.cart_service.dto;

import com.brunonegro.cart_service.model.Cart;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ProductDetailDTO {

    private Long idProductDetail;
    private int detailIndex;
    private Long idProduct;
    private int productQuantity;

    //Datos vivos del product-service, NO se persisten en la BD del carrito
    private String productName;
    private double unitPrice;
    private double subtotal;


}
