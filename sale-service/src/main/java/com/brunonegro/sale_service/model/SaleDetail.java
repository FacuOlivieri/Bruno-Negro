package com.brunonegro.sale_service.model;

import jakarta.persistence.*;
import lombok.*;

/*
 * Foto inmutable de una linea del carrito al momento de la venta.
 * Nombre y precios se copian: si despues cambian en product-service, la venta no se entera.
 */
@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SaleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSaleDetail;
    private int detailIndex;

    @Column(nullable = false)
    private Long idProduct;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int productQuantity;

    @Column(nullable = false)
    private double unitPrice;

    @Column(nullable = false)
    private double subtotal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;
}