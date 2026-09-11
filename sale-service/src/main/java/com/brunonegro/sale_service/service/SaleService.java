package com.brunonegro.sale_service.service;

import com.brunonegro.sale_service.dto.CartDTO;
import com.brunonegro.sale_service.dto.ClientForSaleResponseDTO;
import com.brunonegro.sale_service.dto.SaleDTO;
import com.brunonegro.sale_service.exception.CartNotFoundException;
import com.brunonegro.sale_service.exception.ClientNotFoundException;
import com.brunonegro.sale_service.exception.SaleNotFoundException;
import com.brunonegro.sale_service.model.Sale;
import com.brunonegro.sale_service.repository.ICartAPI;
import com.brunonegro.sale_service.repository.IClientAPI;
import com.brunonegro.sale_service.repository.ISaleRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
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
        ClientForSaleResponseDTO clientData = findClientOrThrow(foundSale.getIdClient());
        CartDTO foundCart = findCartOrThrow(foundSale.getIdCart());

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

        if (sales.isEmpty()) {
            throw new SaleNotFoundException("No se encuentran ventas registradas");
        }

        List<SaleDTO> salesDTOS = new ArrayList<>();

        for(Sale sale : sales){
            SaleDTO saleDTO = SaleDTO.builder()
                    .idSale(sale.getIdSale())
                    .saleDate(sale.getSaleDate())
                    .clientData(findClientOrThrow(sale.getIdClient()))
                    .cart(findCartOrThrow(sale.getIdCart()))
                    .build();

            salesDTOS.add(saleDTO);
        }

        return salesDTOS;

    }

    @Override
    public SaleDTO save(Long idCart) {
        CartDTO foundCart = findCartOrThrow(idCart);

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
                .clientData(findClientOrThrow(saleJustCreated.getIdClient()))
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
        saleToUpdate.setSaleDate(sale.getSaleDate());
        saleToUpdate.setIdCart(sale.getCart().getIdCart());
        saleToUpdate.setIdClient(sale.getCart().getIdUser());
        saleToUpdate.setTotalPrice(sale.getCart().getTotal());
        saleRepository.save(saleToUpdate);

        //Retornamos DTO del mismo
        return SaleDTO.builder()
                .idSale(saleToUpdate.getIdSale())
                .saleDate(saleToUpdate.getSaleDate())
                .clientData(findClientOrThrow(saleToUpdate.getIdClient()))
                .cart(sale.getCart())
                .build();
    }


    ////////////////////////////////  Traduccion de fallas remotas  ////////////////////////////////

    /*
     * Feign lanza FeignException ante cualquier respuesta que no sea 2xx: nunca devuelve null.
     * Por eso el 404 del servicio remoto se atrapa y se traduce a una excepcion del dominio.
     * Las demas FeignException (servicio caido, timeout) suben y las toma el GlobalExceptionHandler.
     */
    private CartDTO findCartOrThrow(Long idCart) {
        try {
            return cartAPI.findById(idCart);
        } catch (FeignException.NotFound e) {
            throw new CartNotFoundException(idCart);
        }
    }

    private ClientForSaleResponseDTO findClientOrThrow(Long idClient) {
        try {
            return clientAPI.findForSaleById(idClient);
        } catch (FeignException.NotFound e) {
            throw new ClientNotFoundException(idClient);
        }
    }


}
