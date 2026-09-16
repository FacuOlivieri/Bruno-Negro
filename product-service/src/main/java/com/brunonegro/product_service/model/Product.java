package com.brunonegro.product_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProduct;
    private String code;
    private String productName;
    private String description;
    private double unitPrice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_category", nullable = false)
    private Category category;

    private String imageUrl;
    private String brand;
    private float firstDescount;
    private float secondDescount;
    private float thirdDescount;

}
