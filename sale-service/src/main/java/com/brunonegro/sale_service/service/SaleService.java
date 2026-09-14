package com.brunonegro.sale_service.service;

import com.brunonegro.sale_service.dto.CartDTO;
import com.brunonegro.sale_service.dto.ClientForSaleResponseDTO;
import com.brunonegro.sale_service.dto.ProductDetailDTO;
import com.brunonegro.sale_service.dto.SaleDTO;
import com.brunonegro.sale_service.exception.CartNotFoundException;
import com.brunonegro.sale_service.exception.ClientNotFoundException;
import com.brunonegro.sale_service.exception.SaleNotFoundException;
import com.brunonegro.sale_service.mapper.SaleMapper;
import com.brunonegro.sale_service.model.Sale;
import com.brunonegro.sale_service.repository.ICartAPI;
import com.brunonegro.sale_service.repository.IClientAPI;
import com.brunonegro.sale_service.repository.ISaleRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //Lee solo de la BD de ventas: el carrito ya no se consulta, las lineas estan congeladas
    @Override
    @Transactional(readOnly = true)
    public SaleDTO findById(long id) {
        Sale foundSale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("No se encuentra la venta en la base de datos"));

        return SaleMapper.toDto(foundSale, findClientOrThrow(foundSale.getIdClient()));
    }


    @Override
    @Transactional(readOnly = true)
    public List<SaleDTO> findAll() {
        List<Sale> sales = saleRepository.findAll();

        if (sales.isEmpty()) {
            throw new SaleNotFoundException("No se encuentran ventas registradas");
        }

        List<SaleDTO> salesDTOS = new ArrayList<>();

        for (Sale sale : sales) {
            salesDTOS.add(SaleMapper.toDto(sale, findClientOrThrow(sale.getIdClient())));
        }

        return salesDTOS;
    }

    @Override
    @Transactional
    public SaleDTO save(Long idCart) {
        CartDTO foundCart = findCartOrThrow(idCart);

        Sale sale = new Sale();
        sale.setIdCart(idCart);
        sale.setSaleDate(LocalDate.now());
        applyCartSnapshot(sale, foundCart);

        //save devuelve la entidad ya con su id: no hace falta volver a buscarla
        Sale savedSale = saleRepository.save(sale);

        return SaleMapper.toDto(savedSale, findClientOrThrow(savedSale.getIdClient()));
    }


    @Override
    @Transactional
    public void delete(Long idSale) {
        Sale sale = saleRepository.findById(idSale)
                .orElseThrow(() -> new SaleNotFoundException("No se encuentra la venta en la base de datos"));
        saleRepository.delete(sale);
    }


    //Vuelve a congelar la venta a partir del carrito indicado; nunca confia en precios que manda el cliente
    @Override
    @Transactional
    public SaleDTO update(Long idSale, SaleDTO sale) {
        Sale saleToUpdate = saleRepository.findById(idSale)
                .orElseThrow(() -> new SaleNotFoundException("No se encuentra la venta en la base de datos"));

        CartDTO foundCart = findCartOrThrow(sale.getIdCart());

        if (sale.getSaleDate() != null) {
            saleToUpdate.setSaleDate(sale.getSaleDate());
        }
        saleToUpdate.setIdCart(sale.getIdCart());
        applyCartSnapshot(saleToUpdate, foundCart);

        Sale updatedSale = saleRepository.save(saleToUpdate);

        return SaleMapper.toDto(updatedSale, findClientOrThrow(updatedSale.getIdClient()));
    }


    ////////////////////////////////  Foto del carrito  ////////////////////////////////

    //Copia cliente, total y lineas del carrito a la venta. Reemplaza las lineas anteriores si las habia
    private void applyCartSnapshot(Sale sale, CartDTO cart) {
        sale.setIdClient(cart.getIdUser());
        sale.setTotalPrice(cart.getTotal());

        //orphanRemoval = true -> clear() borra de la BD las lineas viejas
        sale.getDetails().clear();
        for (ProductDetailDTO cartLine : cart.getProductList()) {
            sale.getDetails().add(SaleMapper.toDetailEntity(cartLine, sale));
        }
    }


    ////////////////////////////////  Traduccion de fallas remotas  ////////////////////////////////

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
