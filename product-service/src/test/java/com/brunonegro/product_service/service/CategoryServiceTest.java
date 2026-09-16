package com.brunonegro.product_service.service;

import com.brunonegro.product_service.dto.CategoryDTO;
import com.brunonegro.product_service.dto.CategoryRequestDTO;
import com.brunonegro.product_service.exception.CategoryAlreadyExistsException;
import com.brunonegro.product_service.exception.CategoryInUseException;
import com.brunonegro.product_service.exception.CategoryNotFoundException;
import com.brunonegro.product_service.exception.InvalidCategoryException;
import com.brunonegro.product_service.model.Category;
import com.brunonegro.product_service.repository.ICategoryRepository;
import com.brunonegro.product_service.repository.IProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private ICategoryRepository categoryRepository;

    @Mock
    private IProductRepository productRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void create_normalizaElNombre() {
        when(categoryRepository.existsByName("HERRAMIENTAS")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category saved = invocation.getArgument(0);
            saved.setIdCategory(1);
            return saved;
        });

        CategoryDTO result = categoryService.create(new CategoryRequestDTO(" herramientas "));

        assertThat(result.getIdCategory()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("HERRAMIENTAS");
    }

    @Test
    void create_conNombreDuplicado_lanzaCategoryAlreadyExists() {
        when(categoryRepository.existsByName("HERRAMIENTAS")).thenReturn(true);

        assertThatThrownBy(() -> categoryService.create(new CategoryRequestDTO("Herramientas")))
                .isInstanceOf(CategoryAlreadyExistsException.class);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void create_conNombreVacio_lanzaInvalidCategory() {
        assertThatThrownBy(() -> categoryService.create(new CategoryRequestDTO("   ")))
                .isInstanceOf(InvalidCategoryException.class);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void delete_categoriaInexistente_lanzaCategoryNotFound() {
        when(categoryRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> categoryService.delete(99))
                .isInstanceOf(CategoryNotFoundException.class);
        verify(categoryRepository, never()).deleteById(anyInt());
    }

    @Test
    void delete_categoriaConProductos_lanzaCategoryInUse() {
        when(categoryRepository.existsById(1)).thenReturn(true);
        when(productRepository.existsByCategory_IdCategory(1)).thenReturn(true);

        assertThatThrownBy(() -> categoryService.delete(1))
                .isInstanceOf(CategoryInUseException.class);
        verify(categoryRepository, never()).deleteById(anyInt());
    }

    @Test
    void delete_categoriaSinProductos_seBorra() {
        when(categoryRepository.existsById(1)).thenReturn(true);
        when(productRepository.existsByCategory_IdCategory(1)).thenReturn(false);

        categoryService.delete(1);

        verify(categoryRepository).deleteById(1);
    }
}
