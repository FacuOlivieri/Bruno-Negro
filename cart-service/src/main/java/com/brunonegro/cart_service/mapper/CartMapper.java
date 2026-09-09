package com.brunonegro.cart_service.mapper;

import com.brunonegro.cart_service.dto.*;
import com.brunonegro.cart_service.model.Cart;
import com.brunonegro.cart_service.model.ProductDetail;

import java.util.ArrayList;
import java.util.List;

public final class CartMapper {

    private CartMapper() {
    }

    public static CartDTO toDto(Cart cart) {
        if (cart == null) {
            return null;
        }

        //Transformamos lista dentro de cart a lista de DTO
        List<ProductDetailDTO> listDTO = cart.getProductList().stream().map(ProductDetailMapper::toDto).toList();

        return CartDTO.builder()
                .idCart(cart.getIdCart())
                .idUser(cart.getIdUser())
                .productList(listDTO)
                .build();
    }

    public static List<CartDTO> toDtoList(List<Cart> carts) {
        List<CartDTO> listDTO = new ArrayList<>();
        for (Cart cart : carts) {
            listDTO.add(toDto(cart));
        }
        return listDTO;
    }





}