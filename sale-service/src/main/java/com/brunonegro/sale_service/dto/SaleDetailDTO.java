package com.brunonegro.sale_service.dto;

import lombok.*;

@Builder @AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class SaleDetailDTO {

    private int detailIndex;
    private Long idProduct;
    private String productName;
    private int productQuantity;
    private double unitPrice;
    private double subtotal;

}