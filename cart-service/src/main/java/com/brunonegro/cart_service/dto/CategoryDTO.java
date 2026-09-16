package com.brunonegro.cart_service.dto;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Builder
public class CategoryDTO {

    private Long idCategory;
    private String name;

}
