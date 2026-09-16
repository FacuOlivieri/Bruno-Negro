package com.brunonegro.product_service.service;

import com.brunonegro.product_service.dto.CategoryDTO;
import com.brunonegro.product_service.dto.CategoryRequestDTO;
import com.brunonegro.product_service.exception.CategoryAlreadyExistsException;
import com.brunonegro.product_service.exception.CategoryInUseException;
import com.brunonegro.product_service.exception.CategoryNotFoundException;
import com.brunonegro.product_service.exception.InvalidCategoryException;
import com.brunonegro.product_service.mapper.CategoryMapper;
import com.brunonegro.product_service.model.Category;
import com.brunonegro.product_service.repository.ICategoryRepository;
import com.brunonegro.product_service.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private ICategoryRepository categoryRepository;

    @Autowired
    private IProductRepository productRepository;


    ///////////////////////////////// GET //////////////////////////////

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return CategoryMapper.toDtoList(categoryRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO findById(int id) {
        return CategoryMapper.toDto(findEntityOrThrow(id));
    }


    ///////////////////////////////// CREATE //////////////////////////////

    @Override
    @Transactional
    public CategoryDTO create(CategoryRequestDTO request) {
        String name = normalizeName(request.getName());
        if (categoryRepository.existsByName(name)) {
            throw new CategoryAlreadyExistsException(name);
        }

        Category newCategory = CategoryMapper.toEntity(request);
        newCategory.setName(name);
        return CategoryMapper.toDto(categoryRepository.save(newCategory));
    }


    ///////////////////////////////// PUT //////////////////////////////

    @Override
    @Transactional
    public CategoryDTO update(int id, CategoryRequestDTO request) {
        Category category = findEntityOrThrow(id);
        String name = normalizeName(request.getName());

        //Renombrar a su propio nombre no es duplicado, por eso se excluye su id
        if (categoryRepository.existsByNameAndIdCategoryNot(name, id)) {
            throw new CategoryAlreadyExistsException(name);
        }

        category.setName(name);
        return CategoryMapper.toDto(categoryRepository.save(category));
    }


    ///////////////////////////////// DELETE //////////////////////////////

    @Override
    @Transactional
    public void delete(int id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }
        validateNotInUse(id);
        categoryRepository.deleteById(id);
    }


    ///////////////////////////////// Helpers de busqueda y validacion //////////////////////////////

    //Busca la categoria en la BD o corta con CategoryNotFoundException
    private Category findEntityOrThrow(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    //Deja el nombre sin espacios de borde y en mayusculas, que es como lo buscan los filtros de productos
    private String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidCategoryException("El nombre de la categoria es obligatorio");
        }
        return name.trim().toUpperCase();
    }

    //Una categoria con productos no se puede borrar: los productos quedarian sin categoria
    private void validateNotInUse(int id) {
        if (productRepository.existsByCategory_IdCategory(id)) {
            throw new CategoryInUseException(id);
        }
    }
}
