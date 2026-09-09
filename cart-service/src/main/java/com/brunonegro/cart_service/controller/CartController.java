package com.brunonegro.cart_service.controller;

import com.brunonegro.cart_service.dto.CartDTO;
import com.brunonegro.cart_service.dto.CartProductRequestDTO;
import com.brunonegro.cart_service.dto.CartRequestDTO;
import com.brunonegro.cart_service.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    ///////////////////////////////////      GET     ///////////////////////////////////

    @GetMapping("/find/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CartDTO> findAll() {
        return cartService.findAll();
    }

    @GetMapping("/find/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CartDTO findById(@PathVariable Long id) {
        return cartService.findById(id);
    }

    @GetMapping("/find/user")
    @ResponseStatus(HttpStatus.OK)
    public CartDTO findByIdUser(@RequestParam Long idUser) {
        return cartService.findByIdUser(idUser);
    }


    ///////////////////////////////////      POST       ///////////////////////////////////

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CartDTO create(@RequestBody CartRequestDTO request) {
        return cartService.create(request);
    }


    ///////////////////////////////////      PUT     ///////////////////////////////////

    @PutMapping("/add/{idCart}")
    @ResponseStatus(HttpStatus.OK)
    public CartDTO addProductToCart(@PathVariable Long idCart, @RequestBody CartProductRequestDTO productRequest) {
        return cartService.addProductToCart(idCart, productRequest);
    }


    ///////////////////////////////////      DELETE     ///////////////////////////////////

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        cartService.delete(id);
    }

    @DeleteMapping("/delete/{idCart}/product/{idProduct}")
    @ResponseStatus(HttpStatus.OK)
    public CartDTO deleteProductFromList(@PathVariable Long idCart, @PathVariable Long idProduct) {
        return cartService.deleteProductFromList(idCart, idProduct);
    }
}