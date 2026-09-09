package com.brunonegro.cart_service.service;

import com.brunonegro.cart_service.dto.*;
import com.brunonegro.cart_service.exception.CartNotFoundException;
import com.brunonegro.cart_service.mapper.CartMapper;
import com.brunonegro.cart_service.mapper.ProductDetailMapper;
import com.brunonegro.cart_service.model.Cart;
import com.brunonegro.cart_service.model.ProductDetail;
import com.brunonegro.cart_service.repository.ICartRepository;
import com.brunonegro.cart_service.repository.IProductAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService implements ICartService {

    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private IProductAPI productAPI;



    ///////////////////////////////// GET //////////////////////////////


    @Override
    public List<CartDTO> findAll() {
        List<Cart> cartsInBD = cartRepository.findAll();

        List<CartDTO> cartDTOList = new ArrayList<>();
        for (Cart cart:cartsInBD){
            CartDTO cartDTO = CartMapper.toDto(cart);
        }

        return cartDTOList;
    }

    @Override
    @Transactional(readOnly = true)
    public CartDTO findById(Long id) {
        return CartMapper.toDto(findEntityOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public CartDTO findByIdUser(Long idUser) {
        return cartRepository.findByIdUser(idUser)
                .map(CartMapper::toDto)
                .orElseThrow(() -> new CartNotFoundException("No existe carrito para el usuario " + idUser));
    }


    ///////////////////////////////// CREATE //////////////////////////////

    @Override
    @Transactional
    public CartDTO create(CartRequestDTO request) {
            Cart newCart = new Cart();


            //Consultar id user de alguna forma
            newCart.setIdUser(request.getIdUser());
            newCart.setProductList(new ArrayList<>());
            cartRepository.save(newCart);

            return CartMapper.toDto(newCart);
    }


    ///////////////////////////////// DELETE //////////////////////////////


    @Override
    @Transactional
    public void delete(Long id) {
        if (!cartRepository.existsById(id)) {
            throw new CartNotFoundException(id);
        }
        cartRepository.deleteById(id);
    }




    ///////////////////////////////// PUT //////////////////////////////

    @Override
    public Cart addProductToCart(Long idCart, CartProductRequestDTO productRequest) {
        Cart clientCart = findEntityOrThrow(idCart);
        ProductDTO foundProduct = productAPI.findById(productRequest.getIdProduct());

        List<ProductDetail> productList = clientCart.getProductList();
    }


    ///////////////////////////////// Helper Finder con Excepcion incluida //////////////////////////////

    private Cart findEntityOrThrow(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException(id));
    }
}