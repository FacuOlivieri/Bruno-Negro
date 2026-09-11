package com.brunonegro.product_service.exception;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class ProductNotFoundResponse {

    private int status;
    private String error;
    private String mensaje;

}
