package com.brunonegro.sale_service.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDTO {

    private Long idSale;
    private LocalDate saleDate;
    private ClientForSaleResponseDTO clientData;       //Buscarlo a traves de la API de clientes
    private Long idCart;                               //Carrito de origen, solo como referencia
    private double totalPrice;                         //Total congelado al momento de la venta
    private List<SaleDetailDTO> details;               //Lineas congeladas al momento de la venta
}