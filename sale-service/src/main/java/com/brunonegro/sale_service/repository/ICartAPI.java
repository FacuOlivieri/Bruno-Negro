package com.brunonegro.sale_service.repository;

import com.brunonegro.sale_service.dto.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart-service")
public interface ICartAPI {

    @GetMapping("/carts/find/{id}")
    public CartDTO findById(@PathVariable Long id);

    @DeleteMapping("/carts/clear/{idCart}")
    public void clear(@PathVariable Long idCart);
}