package com.brunonegro.cart_service.dto;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Builder
public class ProductDTO {

    private String code;
    private String productName;
    private String description;
    private double unitPrice;
    private String category;
    private String imageUrl;
    private String brand;
    private float firstDescount;
    private float secondDescount;
    private float thirdDescount;

}
