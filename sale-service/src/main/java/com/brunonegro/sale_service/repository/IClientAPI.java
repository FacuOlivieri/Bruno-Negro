package com.brunonegro.sale_service.repository;

import com.brunonegro.sale_service.dto.ClientForSaleResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client-service")
public interface IClientAPI {

    @GetMapping("/clients/find/{id}/for-sale")
    ClientForSaleResponseDTO findForSaleById(@PathVariable long id);
}
