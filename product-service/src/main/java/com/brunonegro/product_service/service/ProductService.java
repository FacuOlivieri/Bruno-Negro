package com.brunonegro.product_service.service;

import com.brunonegro.product_service.dto.ProductDTO;
import com.brunonegro.product_service.dto.ProductRequestDTO;
import com.brunonegro.product_service.dto.ProductSummaryDTO;
import com.brunonegro.product_service.exception.CategoryNotFoundException;
import com.brunonegro.product_service.exception.InvalidCategoryException;
import com.brunonegro.product_service.exception.ProductNotFoundException;
import com.brunonegro.product_service.mapper.ProductMapper;
import com.brunonegro.product_service.model.Category;
import com.brunonegro.product_service.model.Product;
import com.brunonegro.product_service.repository.ICategoryRepository;
import com.brunonegro.product_service.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService implements IProductService {

    @Value("${server.port}")
    private int serverPort;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        return ProductMapper.toDtoList(productRepository.findAll());
    }



    @Override
    @Transactional(readOnly = true)
    public ProductDTO findById(int id) throws ProductNotFoundException {
        System.out.println("este producto se consulto por el puerto: " + serverPort);
        return ProductMapper.toDto(findEntityOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO findByCode(String code) {
        return productRepository.findByCode(code)
                .map(ProductMapper::toDto)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with code " + code));
    }

    //Filtro solo por categoria: una categoria existente sin productos devuelve lista vacia, no es error
    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findAllByCategory(String category) {
        Category categoryFound = categoryRepository.findByName(category.trim().toUpperCase())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with name " + category));

        return ProductMapper.toDtoList(productRepository.findProductsByCategory(categoryFound));
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> findAllByCategoryAndBrand(String category, String brand) {
        List<ProductDTO> productList = productRepository.findAllByBrandAndCategory_Name(brand.toUpperCase(),category.toUpperCase())
                .stream()
                .map(ProductMapper::toDto)
                .toList();

        if (productList.isEmpty()){
            throw new ProductNotFoundException("No se encontraron productos con esa categoria o marca");
        }
        return productList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductSummaryDTO> findAllSummary() {
        return ProductMapper.toSummaryDtoList(productRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductSummaryDTO findSummaryById(int id) {
        return ProductMapper.toSummaryDto(findEntityOrThrow(id));
    }

    @Override
    @Transactional
    public ProductDTO create(ProductRequestDTO request) {
        Category category = findCategoryOrThrow(request.getIdCategory());
        Product saved = productRepository.save(ProductMapper.toEntity(request, category));
        return ProductMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ProductDTO update(int id, ProductRequestDTO request) {
        Product product = findEntityOrThrow(id);
        product.setCode(request.getCode());
        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setUnitPrice(request.getUnitPrice());
        product.setCategory(findCategoryOrThrow(request.getIdCategory()));
        product.setImageUrl(request.getImageUrl());
        product.setBrand(request.getBrand());
        product.setFirstDescount(request.getFirstDescount());
        product.setSecondDescount(request.getSecondDescount());
        product.setThirdDescount(request.getThirdDescount());
        return ProductMapper.toDto(productRepository.save(product));
    }

    @Override
    @Transactional
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

    //Todo producto necesita una categoria existente
    private Category findCategoryOrThrow(Integer idCategory) {
        if (idCategory == null) {
            throw new InvalidCategoryException("El producto debe tener una categoria (idCategory)");
        }
        return categoryRepository.findById(idCategory)
                .orElseThrow(() -> new CategoryNotFoundException(idCategory));
    }
}
