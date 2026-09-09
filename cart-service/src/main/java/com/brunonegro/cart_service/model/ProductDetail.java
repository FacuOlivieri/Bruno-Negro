package com.brunonegro.cart_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ProductDetail {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProductDetail;
    private int detailIndex;

    @Column(name = "id_product", nullable = false)
    private Long idProduct;

    @Column(nullable = false) //no puede ser negativa, arreglar
    private int productQuantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

}
