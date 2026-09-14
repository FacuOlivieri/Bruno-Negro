package com.brunonegro.sale_service.mapper;

import com.brunonegro.sale_service.dto.ClientForSaleResponseDTO;
import com.brunonegro.sale_service.dto.ProductDetailDTO;
import com.brunonegro.sale_service.dto.SaleDTO;
import com.brunonegro.sale_service.dto.SaleDetailDTO;
import com.brunonegro.sale_service.model.Sale;
import com.brunonegro.sale_service.model.SaleDetail;

import java.util.ArrayList;
import java.util.List;

public class SaleMapper {

    //Copia una linea del carrito (con precios ya calculados por cart-service) a una linea de la venta
    public static SaleDetail toDetailEntity(ProductDetailDTO cartLine, Sale sale) {
        return SaleDetail.builder()
                .detailIndex(cartLine.getDetailIndex())
                .idProduct(cartLine.getIdProduct())
                .productName(cartLine.getProductName())
                .productQuantity(cartLine.getProductQuantity())
                .unitPrice(cartLine.getUnitPrice())
                .subtotal(cartLine.getSubtotal())
                .sale(sale)
                .build();
    }

    public static SaleDetailDTO toDetailDto(SaleDetail detail) {
        return SaleDetailDTO.builder()
                .detailIndex(detail.getDetailIndex())
                .idProduct(detail.getIdProduct())
                .productName(detail.getProductName())
                .productQuantity(detail.getProductQuantity())
                .unitPrice(detail.getUnitPrice())
                .subtotal(detail.getSubtotal())
                .build();
    }

    //Arma la respuesta solo con lo persistido en sale-service, mas los datos del cliente
    public static SaleDTO toDto(Sale sale, ClientForSaleResponseDTO clientData) {
        List<SaleDetailDTO> details = new ArrayList<>();
        for (SaleDetail detail : sale.getDetails()) {
            details.add(toDetailDto(detail));
        }

        return SaleDTO.builder()
                .idSale(sale.getIdSale())
                .saleDate(sale.getSaleDate())
                .clientData(clientData)
                .idCart(sale.getIdCart())
                .totalPrice(sale.getTotalPrice())
                .details(details)
                .build();
    }
}