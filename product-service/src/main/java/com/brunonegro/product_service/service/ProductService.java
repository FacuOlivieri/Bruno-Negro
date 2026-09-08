package com.brunonegro.product_service.service;

import com.brunonegro.product_service.dto.ProductDTO;
import com.brunonegro.product_service.dto.ProductRequestDTO;
import com.brunonegro.product_service.dto.ProductSummaryDTO;
import com.brunonegro.product_service.exception.ProductNotFoundException;
import com.brunonegro.product_service.mapper.ProductMapper;
import com.brunonegro.product_service.model.Product;
import com.brunonegro.product_service.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Override
    public List<ProductDTO> findAll() {
        return ProductMapper.toDtoList(productRepository.findAll());
    }

    @Override
    public ProductDTO findById(int id) {
        return ProductMapper.toDto(findEntityOrThrow(id));
    }

    @Override
    public ProductDTO findByCode(String code) {
        return productRepository.findByCode(code)
                .map(ProductMapper::toDto)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with code " + code));
    }

    public List<ProductDTO> findAllByCategoryAndBrand(String category, String brand) {
        List<ProductDTO> productList = productRepository.findAllByBrandAndCategory(brand.toUpperCase(),category.toUpperCase())
                .stream()
                .map(ProductMapper::toDto)
                .toList();

        if (productList.isEmpty()){
            throw new ProductNotFoundException("No se encontraron productos con esa categoria o marca");
        }
        return productList;
    }

    @Override
    public List<ProductSummaryDTO> findAllSummary() {
        return ProductMapper.toSummaryDtoList(productRepository.findAll());
    }

    @Override
    public ProductSummaryDTO findSummaryById(int id) {
        return ProductMapper.toSummaryDto(findEntityOrThrow(id));
    }

    @Override
    public ProductDTO create(ProductRequestDTO request) {
        Product saved = productRepository.save(ProductMapper.toEntity(request));
        return ProductMapper.toDto(saved);
    }

    @Override
    public ProductDTO update(int id, ProductRequestDTO request) {
        Product product = findEntityOrThrow(id);
        product.setCode(request.getCode());
        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setUnitPrice(request.getUnitPrice());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());
        product.setBrand(request.getBrand());
        product.setFirstDescount(request.getFirstDescount());
        product.setSecondDescount(request.getSecondDescount());
        product.setThirdDescount(request.getThirdDescount());
        return ProductMapper.toDto(productRepository.save(product));
    }

    @Override
    public void delete(int id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    private Product findEntityOrThrow(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
