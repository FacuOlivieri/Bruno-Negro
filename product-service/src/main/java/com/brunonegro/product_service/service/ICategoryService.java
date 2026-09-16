package com.brunonegro.product_service.service;

import com.brunonegro.product_service.dto.CategoryDTO;
import com.brunonegro.product_service.dto.CategoryRequestDTO;

import java.util.List;

public interface ICategoryService {

    List<CategoryDTO> findAll();

    CategoryDTO findById(int id);

    CategoryDTO create(CategoryRequestDTO request);

    CategoryDTO update(int id, CategoryRequestDTO request);

    void delete(int id);
}
