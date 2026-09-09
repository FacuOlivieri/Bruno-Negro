package com.brunonegro.cart_service.mapper;

import com.brunonegro.cart_service.dto.ProductDetailDTO;
import com.brunonegro.cart_service.model.ProductDetail;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailMapper {

    public static ProductDetailDTO toDto(ProductDetail productDetail) {
        return ProductDetailDTO.builder()
                .idProductDetail(productDetail.getIdProductDetail())
                .detailIndex(productDetail.getDetailIndex())
                .idProduct(productDetail.getIdProduct())
                .productQuantity(productDetail.getProductQuantity())
                .build();
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
