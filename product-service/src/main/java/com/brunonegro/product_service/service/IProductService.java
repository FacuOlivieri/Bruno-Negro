package com.brunonegro.product_service.service;

import com.brunonegro.product_service.dto.ProductDTO;
import com.brunonegro.product_service.dto.ProductRequestDTO;
import com.brunonegro.product_service.dto.ProductSummaryDTO;

import java.util.List;

public interface IProductService {

    List<ProductDTO> findAll();

    ProductDTO findById(int id) throws Exception;

    ProductDTO findByCode(String code);

    List<ProductSummaryDTO> findAllSummary();

    ProductSummaryDTO findSummaryById(int id);

    ProductDTO create(ProductRequestDTO request);

    ProductDTO update(int id, ProductRequestDTO request);

    void delete(int id);
}
