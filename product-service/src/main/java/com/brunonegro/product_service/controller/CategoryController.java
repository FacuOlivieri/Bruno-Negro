package com.brunonegro.product_service.controller;

import com.brunonegro.product_service.dto.CategoryDTO;
import com.brunonegro.product_service.dto.CategoryRequestDTO;
import com.brunonegro.product_service.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    ///////////////////////////////////      GET     ///////////////////////////////////

    @GetMapping("/find/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDTO> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/find/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO findById(@PathVariable int id) {
        return categoryService.findById(id);
    }


    ///////////////////////////////////      POST       ///////////////////////////////////

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO create(@RequestBody CategoryRequestDTO request) {
        return categoryService.create(request);
    }


    ///////////////////////////////////      PUT     ///////////////////////////////////

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO update(@PathVariable int id, @RequestBody CategoryRequestDTO request) {
        return categoryService.update(id, request);
    }


    ///////////////////////////////////      DELETE     ///////////////////////////////////

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        categoryService.delete(id);
    }
}
