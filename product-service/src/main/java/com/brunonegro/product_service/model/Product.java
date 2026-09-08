package com.brunonegro.product_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProduct;
    private String code;
    private String productName;
    private String description;
    private String unitPrice;
    private String category;
    private String imageUrl;
    private String brand;
    private float firstDescount;
    private float secondDescount;
    private float thirdDescount;

}
