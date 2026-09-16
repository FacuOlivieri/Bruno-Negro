package com.brunonegro.product_service.service;

import com.brunonegro.product_service.dto.ProductDTO;
import com.brunonegro.product_service.dto.ProductRequestDTO;
import com.brunonegro.product_service.exception.CategoryNotFoundException;
import com.brunonegro.product_service.exception.InvalidCategoryException;
import com.brunonegro.product_service.model.Category;
import com.brunonegro.product_service.model.Product;
import com.brunonegro.product_service.repository.ICategoryRepository;
import com.brunonegro.product_service.repository.IProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private ICategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void create_sinIdCategory_lanzaInvalidCategory() {
        ProductRequestDTO request = ProductRequestDTO.builder().code("A-100").build();

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(InvalidCategoryException.class);
        verify(productRepository, never()).save(any());
    }

    @Test
    void create_conCategoriaInexistente_lanzaCategoryNotFound() {
        ProductRequestDTO request = ProductRequestDTO.builder().code("A-100").idCategory(999).build();
        when(categoryRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(CategoryNotFoundException.class);
        verify(productRepository, never()).save(any());
    }

    @Test
    void findAllByCategory_categoriaInexistente_lanzaCategoryNotFound() {
        when(categoryRepository.findByName("INEXISTENTE")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findAllByCategory("inexistente"))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    void findAllByCategory_categoriaExistente_filtraPorEsaCategoria() {
        Category herramientas = Category.builder().idCategory(1).name("HERRAMIENTAS").build();
        Product taladro = Product.builder().idProduct(10).productName("Taladro").category(herramientas).build();
        when(categoryRepository.findByName("HERRAMIENTAS")).thenReturn(Optional.of(herramientas));
        when(productRepository.findProductsByCategory(herramientas)).thenReturn(List.of(taladro));

        List<ProductDTO> result = productService.findAllByCategory(" herramientas ");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory().getName()).isEqualTo("HERRAMIENTAS");
    }
}
