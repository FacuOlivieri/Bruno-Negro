package com.brunonegro.sale_service.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDTO {

    private Long idSale;
    private LocalDate saleDate;
    private ClientForSaleResponseDTO clientData;       //Buscarlo a traves de la API de clientes
    private CartDTO cart;

    /*
    CartDTO compagina los siguientes atributos:

    private Long idCart;
    private Long idUser;
    private List<ProductDetailDTO> productList;
    private double total;
     */
}
