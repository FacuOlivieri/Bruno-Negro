package com.brunonegro.cart_service.mapper;

import com.brunonegro.cart_service.dto.*;
import com.brunonegro.cart_service.model.Cart;
import com.brunonegro.cart_service.helper.PricesHelper;
import com.brunonegro.cart_service.model.ProductDetail;

import java.util.ArrayList;
import java.util.List;

public final class CartMapper {

    private CartMapper() {
    }

    //Mapea el carrito con los datos crudos de la BD, sin nombres ni precios
    public static CartDTO toDto(Cart cart) {
        if (cart == null) {
            return null;
        }

        //Transformamos lista dentro de cart a lista de DTO
        List<ProductDetailDTO> listDTO = cart.getProductList().stream().map(ProductDetailMapper::toDto).toList();

        return toDto(cart, listDTO);
    }

    //Mapea el carrito usando una lista de detalles ya recalculada, y suma el total
    public static CartDTO toDto(Cart cart, List<ProductDetailDTO> productList) {
        if (cart == null) {
            return null;
        }

        double total = PricesHelper.calculateTotal(productList);

        return CartDTO.builder()
                .idCart(cart.getIdCart())
                .idUser(cart.getIdUser())
                .productList(productList)
                .total(total)
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