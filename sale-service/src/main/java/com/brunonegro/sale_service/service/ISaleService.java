package com.brunonegro.sale_service.service;

import com.brunonegro.sale_service.dto.SaleDTO;
import com.brunonegro.sale_service.model.Sale;

import java.time.LocalDate;
import java.util.List;

public interface ISaleService {

    Sale findById(long id);
    List<Sale> findAll();
    SaleDTO save(Long idCarrito);
    void delete(Long idSale);
    Sale update(Long idSale, Sale sale);
}
