package com.brunonegro.product_service.controller;

import com.brunonegro.product_service.dto.ProductDTO;
import com.brunonegro.product_service.dto.ProductRequestDTO;
import com.brunonegro.product_service.dto.ProductSummaryDTO;
import com.brunonegro.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    ///////////////////////////////////      GET: Filtro de Productos COMPLETOS     ///////////////////////////////////

    @GetMapping("/find/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> findAll() {
        return productService.findAll();
    }

    @GetMapping("/find/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO findById(@PathVariable int id) {
        return productService.findById(id);
    }

    @GetMapping("/find/code")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO findByCode(@RequestParam String code) {
        return productService.findByCode(code);
    }


    @GetMapping("/find/filter")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> findByCategoryAndBrand(@RequestParam String brand,
                                             @RequestParam String category) {
        return productService.findAllByCategoryAndBrand(category, brand);
    }


    ///////////////////////////////////      GET: Filtro de Productos SUMMARY     ///////////////////////////////////

    @GetMapping("/find/summary/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductSummaryDTO> findAllSummary() {
        return productService.findAllSummary();
    }

    @GetMapping("/find/summary/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductSummaryDTO findSummaryById(@PathVariable int id) {
        return productService.findSummaryById(id);
    }



    ///////////////////////////////////      POST       ///////////////////////////////////

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@RequestBody ProductRequestDTO request) {
        return productService.create(request);
    }


    ///////////////////////////////////      PUT     ///////////////////////////////////

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO update(@PathVariable int id, @RequestBody ProductRequestDTO request) {
        return productService.update(id, request);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        productService.delete(id);
    }
}
