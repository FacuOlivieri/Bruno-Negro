package com.brunonegro.cart_service.repository;

import com.brunonegro.cart_service.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface IProductAPI {

    @GetMapping("products/find/{id}")
    public ProductDTO findById(@PathVariable Long id);

}
