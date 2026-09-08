package com.brunonegro.product_service.mapper;

import com.brunonegro.product_service.dto.ProductDTO;
import com.brunonegro.product_service.dto.ProductRequestDTO;
import com.brunonegro.product_service.dto.ProductSummaryDTO;
import com.brunonegro.product_service.model.Product;

import java.util.List;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static ProductDTO toDto(Product product) {
        if (product == null) {
            return null;
        }
        return ProductDTO.builder()
                .idProduct(product.getIdProduct())
                .code(product.getCode())
                .productName(product.getProductName())
                .description(product.getDescription())
                .unitPrice(product.getUnitPrice())
                .category(product.getCategory())
                .imageUrl(product.getImageUrl())
                .brand(product.getBrand())
                .firstDescount(product.getFirstDescount())
                .secondDescount(product.getSecondDescount())
                .thirdDescount(product.getThirdDescount())
                .build();
    }

    public static List<ProductDTO> toDtoList(List<Product> products) {
        return products.stream()
                .map(ProductMapper::toDto)
                .toList();
    }

    public static ProductSummaryDTO toSummaryDto(Product product) {
        if (product == null) {
            return null;
        }
        return ProductSummaryDTO.builder()
                .code(product.getCode())
                .productName(product.getProductName())
                .unitPrice(product.getUnitPrice())
                .brand(product.getBrand())
                .build();
    }

    public static List<ProductSummaryDTO> toSummaryDtoList(List<Product> products) {
        return products.stream()
                .map(ProductMapper::toSummaryDto)
                .toList();
    }

    public static Product toEntity(ProductRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Product.builder()
                .code(dto.getCode())
                .productName(dto.getProductName())
                .description(dto.getDescription())
                .unitPrice(dto.getUnitPrice())
                .category(dto.getCategory())
                .imageUrl(dto.getImageUrl())
                .brand(dto.getBrand())
                .firstDescount(dto.getFirstDescount())
                .secondDescount(dto.getSecondDescount())
                .thirdDescount(dto.getThirdDescount())
                .build();
    }
}
