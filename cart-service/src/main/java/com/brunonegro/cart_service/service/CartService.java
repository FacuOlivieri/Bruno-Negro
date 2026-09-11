package com.brunonegro.cart_service.service;

import com.brunonegro.cart_service.dto.*;
import com.brunonegro.cart_service.exception.CartNotFoundException;
import com.brunonegro.cart_service.helper.PricesHelper;
import com.brunonegro.cart_service.exception.InvalidQuantityException;
import com.brunonegro.cart_service.exception.ProductNotFoundException;
import com.brunonegro.cart_service.mapper.CartMapper;
import com.brunonegro.cart_service.mapper.ProductDetailMapper;
import com.brunonegro.cart_service.model.Cart;
import com.brunonegro.cart_service.model.ProductDetail;
import com.brunonegro.cart_service.repository.ICartRepository;
import com.brunonegro.cart_service.repository.IProductAPI;
import com.brunonegro.cart_service.validator.ClientValidator;
import feign.FeignException;
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

    @Autowired
    private ClientValidator clientValidator;



    ///////////////////////////////// GET //////////////////////////////


    @Override
    @Transactional(readOnly = true)
    public List<CartDTO> findAll() {
        List<Cart> cartsInBD = cartRepository.findAll();

        List<CartDTO> cartDTOList = new ArrayList<>();
        for (Cart cart : cartsInBD) {
            cartDTOList.add(recalculate(cart));
        }
        return cartDTOList;
    }

    @Override
    @Transactional(readOnly = true)
    public CartDTO findById(Long id) {
        return recalculate(findEntityOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public CartDTO findByIdUser(Long idUser) {
        return cartRepository.findByIdUser(idUser)
                .map(this::recalculate)
                .orElseThrow(() -> new CartNotFoundException("No existe carrito para el usuario " + idUser));
    }


    ///////////////////////////////// CREATE //////////////////////////////

    @Override
    @Transactional
    public CartDTO create(CartRequestDTO request) {
            //Corta antes de crear nada si el cliente no existe en client-service
            clientValidator.validateExists(request.getIdUser());

            Cart newCart = new Cart();
            newCart.setIdUser(request.getIdUser());
            newCart.setProductList(new ArrayList<>());
            cartRepository.save(newCart);

            return recalculate(newCart);
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

    //Agrega un producto al carrito, o le suma cantidad si ya estaba, y devuelve el carrito recalculado
    @Override
    @Transactional
    public CartDTO addProductToCart(Long idCart, CartProductRequestDTO productRequest) {
        validateQuantity(productRequest.getQuantity());
        Cart clientCart = findEntityOrThrow(idCart);
        findProductOrThrow(productRequest.getIdProduct()); //validamos que exista en product-service

        ProductDetail existingDetail = findDetailByProduct(clientCart, productRequest.getIdProduct());

        if (existingDetail != null) {
            existingDetail.setProductQuantity(existingDetail.getProductQuantity() + productRequest.getQuantity());
        } else {
            clientCart.getProductList().add(buildDetail(clientCart, productRequest));
        }

        reindexDetails(clientCart.getProductList());

        return recalculate(cartRepository.save(clientCart));
    }

    //Saca un producto del carrito, corre los detailIndex que quedaron y devuelve el carrito recalculado
    @Override
    @Transactional
    public CartDTO deleteProductFromList(Long idCart, Long idProduct) {
        Cart clientCart = findEntityOrThrow(idCart);

        ProductDetail detailToRemove = findDetailByProduct(clientCart, idProduct);
        if (detailToRemove == null) {
            throw new ProductNotFoundException("El producto " + idProduct + " no esta en el carrito " + idCart);
        }

        //orphanRemoval = true -> sacarlo de la lista alcanza para que se borre en la BD
        clientCart.getProductList().remove(detailToRemove);

        reindexDetails(clientCart.getProductList());

        return recalculate(cartRepository.save(clientCart));
    }


    ///////////////////////////////// Recalculo (nombre, subtotal y total) //////////////////////////////

    //Arma el CartDTO pidiendo nombre y precio actual de cada producto al product-service
    private CartDTO recalculate(Cart cart) {
        List<ProductDetailDTO> recalculatedList = new ArrayList<>();

        for (ProductDetail detail : cart.getProductList()) {
            recalculatedList.add(recalculateDetail(detail));
        }

        return CartMapper.toDto(cart, recalculatedList);
    }

    //Convierte una linea del carrito en DTO con el nombre y el subtotal del momento
    private ProductDetailDTO recalculateDetail(ProductDetail detail) {
        ProductDTO product = findProductOrThrow(detail.getIdProduct());
        double unitPrice = PricesHelper.calculatePriceWithDescount(product);
        return ProductDetailMapper.toDto(detail, product, unitPrice);
    }

    //Renumera las lineas de 1 a N segun la posicion que quedaron en la lista
    private void reindexDetails(List<ProductDetail> productList) {
        for (int i = 0; i < productList.size(); i++) {
            productList.get(i).setDetailIndex(i + 1);
        }
    }


    ///////////////////////////////// Helpers de busqueda y validacion //////////////////////////////

    //Busca el carrito en la BD o corta con CartNotFoundException
    private Cart findEntityOrThrow(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException(id));
    }

    //Pide el producto al product-service; el 404 de Feign lo traducimos a ProductNotFoundException
    private ProductDTO findProductOrThrow(Long idProduct) {
        try {
            return productAPI.findById(idProduct);
        } catch (FeignException.NotFound e) {
            throw new ProductNotFoundException(idProduct);
        }
    }

    //Devuelve la linea del carrito de ese producto, o null si el producto no esta cargado
    private ProductDetail findDetailByProduct(Cart cart, Long idProduct) {
        return cart.getProductList().stream()
                .filter(detail -> detail.getIdProduct().equals(idProduct))
                .findFirst()
                .orElse(null);
    }

    //Valida que la cantidad pedida sea mayor a cero
    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException(quantity);
        }
    }

    //Crea la linea nueva enlazada al carrito; el detailIndex lo asigna reindexDetails
    private ProductDetail buildDetail(Cart cart, CartProductRequestDTO request) {
        return ProductDetail.builder()
                .idProduct(request.getIdProduct())
                .productQuantity(request.getQuantity())
                .cart(cart)
                .build();
    }
}
