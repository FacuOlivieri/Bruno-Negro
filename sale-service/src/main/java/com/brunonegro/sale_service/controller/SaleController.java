package com.brunonegro.sale_service.controller;

import com.brunonegro.sale_service.dto.SaleDTO;
import com.brunonegro.sale_service.model.Sale;
import com.brunonegro.sale_service.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @GetMapping("/find/all")
    @ResponseStatus(HttpStatus.OK)
    public List<SaleDTO> findAll() {
        return saleService.findAll();
    }

    @GetMapping("/find/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SaleDTO findById(@PathVariable Long id) {
        return saleService.findById(id);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public SaleDTO create(@RequestParam Long idCarrito) {
        return saleService.save(idCarrito);
    }

    @PutMapping("/update/{idSale}")
    @ResponseStatus(HttpStatus.OK)
    public SaleDTO update(@PathVariable Long idSale, @RequestBody SaleDTO sale) {
        return saleService.update(idSale, sale);
    }

    @DeleteMapping("/delete/{idSale}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long idSale) {
        saleService.delete(idSale);

    }
}
