package com.brunonegro.sale_service.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
@Builder
public class ErrorHandler {

    private int statusCode;
    private String error;
    private String message;


}
