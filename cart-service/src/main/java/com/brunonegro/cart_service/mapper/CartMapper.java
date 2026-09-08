package com.brunonegro.cart_service.mapper;

import com.brunonegro.cart_service.dto.CartDTO;
import com.brunonegro.cart_service.dto.CartProductDTO;
import com.brunonegro.cart_service.dto.CartProductRequestDTO;
import com.brunonegro.cart_service.dto.CartRequestDTO;
import com.brunonegro.cart_service.model.Cart;
import com.brunonegro.cart_service.model.CartProduct;

import java.util.List;

public final class CartMapper {

    private CartMapper() {
    }

    public static CartDTO toDto(Cart cart) {
        if (cart == null) {
            return null;
        }
        // TODO: integracion con product-service.
        // Aca es donde se enriquece cada linea con los datos del producto
        // (nombre, precio unitario, subtotal) y se calcula el total del carrito.
        // product-service ya expone GET /products/find/summary/{id}, que devuelve
        // code, productName, unitPrice y brand: es la opcion barata para esto.
        // Ojo: ese endpoint recibe un int, y aca idProduct es Long.
        return CartDTO.builder()
                .idCart(cart.getIdCart())
                .idUser(cart.getIdUser())
                .productList(toProductDtoList(cart.getProductList()))
                .build();
    }

    public static List<CartDTO> toDtoList(List<Cart> carts) {
        return carts.stream()
                .map(CartMapper::toDto)
                .toList();
    }

    public static CartProductDTO toProductDto(CartProduct cartProduct) {
        if (cartProduct == null) {
            return null;
        }
        return CartProductDTO.builder()
                .idCartProduct(cartProduct.getIdCartProduct())
                .idProduct(cartProduct.getIdProduct())
                .quantity(cartProduct.getQuantity())
                .build();
    }

    public static List<CartProductDTO> toProductDtoList(List<CartProduct> cartProducts) {
        if (cartProducts == null) {
            return List.of();
        }
        return cartProducts.stream()
                .map(CartMapper::toProductDto)
                .toList();
    }

    public static Cart toEntity(CartRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Cart cart = Cart.builder()
                .idUser(dto.getIdUser())
                .build();
        cart.getProductList().addAll(toProductEntities(dto.getProductList(), cart));
        return cart;
    }

    // El carrito padre se pasa por parametro porque CartProduct.cart es
    // @JoinColumn(nullable = false): sin esa referencia hacia atras el insert
    // falla con cart_id en null.
    public static List<CartProduct> toProductEntities(List<CartProductRequestDTO> dtoList, Cart cart) {
        if (dtoList == null) {
            return List.of();
        }
        return dtoList.stream()
                .map(dto -> toProductEntity(dto, cart))
                .toList();
    }

    public static CartProduct toProductEntity(CartProductRequestDTO dto, Cart cart) {
        if (dto == null) {
            return null;
        }
        return CartProduct.builder()
                .cart(cart)
                .idProduct(dto.getIdProduct())
                .quantity(dto.getQuantity())
                .build();
    }
}