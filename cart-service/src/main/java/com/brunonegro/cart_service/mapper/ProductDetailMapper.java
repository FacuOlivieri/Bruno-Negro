package com.brunonegro.cart_service.mapper;

import com.brunonegro.cart_service.dto.ProductDetailDTO;
import com.brunonegro.cart_service.dto.ProductDTO;
import com.brunonegro.cart_service.helper.PricesHelper;
import com.brunonegro.cart_service.model.ProductDetail;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailMapper {

    //Mapea solo lo que vive en la BD del carrito (sin nombre ni precios)
    public static ProductDetailDTO toDto(ProductDetail productDetail) {
        return ProductDetailDTO.builder()
                .idProductDetail(productDetail.getIdProductDetail())
                .detailIndex(productDetail.getDetailIndex())
                .idProduct(productDetail.getIdProduct())
                .productQuantity(productDetail.getProductQuantity())
                .build();
    }

    //Mapea el detalle y le agrega el nombre del product-service y el precio ya calculado
    public static ProductDetailDTO toDto(ProductDetail productDetail, ProductDTO product, double unitPrice) {
        ProductDetailDTO dto = toDto(productDetail);
        dto.setProductName(product.getProductName());
        dto.setUnitPrice(unitPrice);
        dto.setSubtotal(PricesHelper.calculateSubtotal(unitPrice, productDetail.getProductQuantity()));
        return dto;
    }


    public static List<ProductDetailDTO> toDtoList(List<ProductDetail> productDetailList) {
        List<ProductDetailDTO> listDTO = new ArrayList<>();
        for (ProductDetail productDetail : productDetailList) {
            listDTO.add(toDto(productDetail));
        }
        return listDTO;
    }



    public static ProductDetail toEntity(ProductDetailDTO productDetailDTO) {
        return ProductDetail.builder()
                .idProductDetail(productDetailDTO.getIdProductDetail())
                .detailIndex(productDetailDTO.getDetailIndex())
                .idProduct(productDetailDTO.getIdProduct())
                .productQuantity(productDetailDTO.getProductQuantity())
                //cart
                .build();
    }

}
