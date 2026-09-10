package com.brunonegro.sale_service.dto;

import lombok.*;

@Builder @AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class ProductDetailDTO {

    private Long idProductDetail;
    private int detailIndex;
    private Long idProduct;
    private int productQuantity;
    private String productName;
    private double unitPrice;
    private double subtotal;

}
