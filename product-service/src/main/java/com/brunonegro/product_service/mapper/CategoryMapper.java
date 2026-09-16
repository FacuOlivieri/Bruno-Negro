package com.brunonegro.product_service.mapper;

import com.brunonegro.product_service.dto.CategoryDTO;
import com.brunonegro.product_service.dto.CategoryRequestDTO;
import com.brunonegro.product_service.model.Category;

import java.util.List;

public final class CategoryMapper {

    private CategoryMapper() {
    }

    public static CategoryDTO toDto(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryDTO.builder()
                .idCategory(category.getIdCategory())
                .name(category.getName())
                .build();
    }

    public static List<CategoryDTO> toDtoList(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toDto)
                .toList();
    }

    public static Category toEntity(CategoryRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Category.builder()
                .name(dto.getName())
                .build();
    }
}
