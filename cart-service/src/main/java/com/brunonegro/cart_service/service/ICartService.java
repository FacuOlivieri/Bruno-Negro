package com.brunonegro.cart_service.service;

import com.brunonegro.cart_service.dto.CartDTO;
import com.brunonegro.cart_service.dto.CartProductRequestDTO;
import com.brunonegro.cart_service.dto.CartRequestDTO;
import com.brunonegro.cart_service.model.Cart;

import java.util.List;

public interface ICartService {

    List<CartDTO> findAll();

    CartDTO findById(Long id);

    CartDTO findByIdUser(Long idUser);

    CartDTO create(CartRequestDTO request);

    void delete(Long id);

    Cart addProductToCart(Long idCart, CartProductRequestDTO productRequest);
}