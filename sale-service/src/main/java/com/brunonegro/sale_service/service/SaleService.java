package com.brunonegro.sale_service.service;

import com.brunonegro.sale_service.dto.CartDTO;
import com.brunonegro.sale_service.dto.ClientForSaleResponseDTO;
import com.brunonegro.sale_service.dto.SaleDTO;
import com.brunonegro.sale_service.exception.SaleNotFoundException;
import com.brunonegro.sale_service.model.Sale;
import com.brunonegro.sale_service.repository.ICartAPI;
import com.brunonegro.sale_service.repository.IClientAPI;
import com.brunonegro.sale_service.repository.ISaleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SaleService implements ISaleService {


    @Autowired
    private ISaleRepository saleRepository;

    @Autowired
    private ICartAPI cartAPI;

    @Autowired
    private IClientAPI clientAPI;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public SaleDTO findById(long id) {
        Sale foundSale = saleRepository.findById(id).orElseThrow(() -> new SaleNotFoundException("No se encuentra la venta recien realizada en la base de datos"));
        ClientForSaleResponseDTO clientData = clientAPI.findForSaleById(foundSale.getIdSale());
        CartDTO foundCart = cartAPI.findById(foundSale.getIdCart());

        return SaleDTO.builder()
                .idSale(foundSale.getIdSale())
                .saleDate(foundSale.getSaleDate())
                .clientData(clientData)
                .cart(foundCart)
                .build();
    }


    @Override
    public List<SaleDTO> findAll() {
        List<Sale> sales = saleRepository.findAll();
        List<SaleDTO> salesDTOS = new ArrayList<>();

        for(Sale sale : sales){
            SaleDTO saleDTO = SaleDTO.builder()
                    .idSale(sale.getIdSale())
                    .saleDate(sale.getSaleDate())
                    .clientData(clientAPI.findForSaleById(sale.getIdSale()))
                    .cart(cartAPI.findById(sale.getIdSale()))
                    .build();

            salesDTOS.add(saleDTO);
        }

        return salesDTOS;

    }

    @Override
    public SaleDTO save(Long idCart) {
        CartDTO foundCart = cartAPI.findById(idCart);
        
        //Guarda datos de entidad en BD
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
        Sale sale = saleRepository.findById(idSale)
                .orElseThrow(() -> new SaleNotFoundException("No se encuentra la venta en la base de datos"));
        saleRepository.deleteById(sale.getIdSale());
    }


    @Override
    public SaleDTO update(Long idSale, SaleDTO sale) {
        Sale saleToUpdate = saleRepository.findById(idSale)
                .orElseThrow(() -> new SaleNotFoundException("No se encuentra la venta en la base de datos"));

        //Guardamos en BD la entidad
        saleToUpdate.setIdCart(sale.getIdSale());
        saleToUpdate.setSaleDate(sale.getSaleDate());
        saleToUpdate.setIdCart(sale.getCart().getIdCart());
        saleToUpdate.setIdClient(sale.getCart().getIdUser());
        saleToUpdate.setTotalPrice(sale.getCart().getTotal());
        saleRepository.save(saleToUpdate);

        //Retornamos DTO del mismo
        return SaleDTO.builder()
                .idSale(saleToUpdate.getIdSale())
                .saleDate(saleToUpdate.getSaleDate())
                .clientData(clientAPI.findForSaleById(saleToUpdate.getIdClient()))
                .cart(sale.getCart())
                .build();
    }





}
