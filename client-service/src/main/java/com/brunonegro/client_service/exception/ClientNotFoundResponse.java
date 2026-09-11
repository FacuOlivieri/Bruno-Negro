package com.brunonegro.client_service.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientNotFoundResponse {

    private int status;
    private String message;
    private String error;
}
