package com.brunonegro.sale_service.service;

import com.brunonegro.sale_service.dto.SaleDTO;
import com.brunonegro.sale_service.model.Sale;

import java.time.LocalDate;
import java.util.List;

public interface ISaleService {

    SaleDTO findById(long id);
    List<SaleDTO> findAll();
    SaleDTO save(Long idCarrito);
    void delete(Long idSale);
    SaleDTO update(Long idSale, SaleDTO sale);
}
