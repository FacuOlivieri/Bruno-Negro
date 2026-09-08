package com.brunonegro.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

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
