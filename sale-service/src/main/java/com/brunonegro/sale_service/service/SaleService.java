package com.brunonegro.sale_service.service;

import com.brunonegro.sale_service.dto.CartDTO;
import com.brunonegro.sale_service.dto.SaleDTO;
import com.brunonegro.sale_service.exception.SaleNotFoundException;
import com.brunonegro.sale_service.model.Sale;
import com.brunonegro.sale_service.repository.ICartAPI;
import com.brunonegro.sale_service.repository.IClientAPI;
import com.brunonegro.sale_service.repository.ISaleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

public class SaleService implements ISaleService {


    @Autowired
    private ISaleRepository saleRepository;

    @Autowired
    private ICartAPI cartAPI;

    @Autowired
    private IClientAPI clientAPI;

    /*
     *
     * Corregir: que devuelva un DTO
     *
     */

    @Override
    public Sale findById(long id) {
        return saleRepository.findById(id).orElse(null);
    }

    /*
     *
     * Corregir: que devuelva un DTO
     *
     */

    @Override
    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    @Override
    public SaleDTO save(Long idCart) {
        CartDTO foundCart = cartAPI.findById(idCart);

        Sale sale = new Sale();
        sale.setIdClient(foundCart.getIdUser());
        sale.setIdCart(idCart);
        sale.setSaleDate(LocalDate.now());
        sale.setTotalPrice(foundCart.getTotal());
        saleRepository.save(sale);


        //Creamos SaleDTO para la response
        Sale saleJustCreated = saleRepository.findFirstByOrderByIdSaleDesc()
                .orElseThrow(() -> new SaleNotFoundException("No se encuentra la venta recien realizada en la base de datos"));


        return SaleDTO.builder()
                .idSale(saleJustCreated.getIdSale())
                .saleDate(saleJustCreated.getSaleDate())
                .clientData(clientAPI.findForSaleById(saleJustCreated.getIdClient()))
                .cart(foundCart)
                .build();

    }


    @Override
    public void delete(Long idSale) {
        saleRepository.deleteById(idSale);
    }

    /*
    *
    * Corregir: que devuelva un DTO
    *
    */

    @Override
    public Sale update(Long idSale, Sale sale) {
        Sale saleToUpdate = saleRepository.findById(idSale).orElse(sale);
        saleToUpdate.setIdCart(sale.getIdCart());
        sale.setSaleDate(saleToUpdate.getSaleDate());

        return saleRepository.save(sale);
    }





}
