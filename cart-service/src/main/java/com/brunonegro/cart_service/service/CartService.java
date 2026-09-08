package com.brunonegro.cart_service.service;

import com.brunonegro.cart_service.dto.CartDTO;
import com.brunonegro.cart_service.dto.CartRequestDTO;
import com.brunonegro.cart_service.exception.CartNotFoundException;
import com.brunonegro.cart_service.mapper.CartMapper;
import com.brunonegro.cart_service.model.Cart;
import com.brunonegro.cart_service.repository.ICartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService implements ICartService {

    @Autowired
    private ICartRepository cartRepository;

    // TODO: integracion con product-service.
    // Aca va a ir inyectado el cliente Feign, algo como:
    //     @Autowired
    //     private IProductClient productClient;
    // Antes de eso hay que agregar spring-cloud-starter-loadbalancer al pom.xml
    // (client-service y product-service ya lo tienen, cart-service no). Sin esa
    // dependencia Feign no puede resolver "product-service" por nombre via Eureka.

    @Override
    @Transactional(readOnly = true)
    public List<CartDTO> findAll() {
        return CartMapper.toDtoList(cartRepository.findAll());
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

    @Override
    @Transactional
    public CartDTO create(CartRequestDTO request) {
        // TODO: integracion con product-service.
        // Validar aca que cada idProduct del request exista en product-service
        // antes de guardar. Si no existe, lanzar una excepcion propia.




        Cart saved = cartRepository.save(CartMapper.toEntity(request));
        return CartMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CartDTO update(Long id, CartRequestDTO request) {
        Cart cart = findEntityOrThrow(id);
        cart.setIdUser(request.getIdUser());

        // TODO: integracion con product-service.
        // Misma validacion que en create(): chequear que cada idProduct exista
        // antes de reemplazar la lista.

        // No usar cart.setProductList(...): la coleccion es cascade = ALL con
        // orphanRemoval = true, y si se reemplaza la instancia Hibernate tira
        // "A collection with cascade=all-delete-orphan was no longer referenced
        // by the owning entity instance". Hay que mutar la coleccion existente.
        cart.getProductList().clear();
        cart.getProductList().addAll(CartMapper.toProductEntities(request.getProductList(), cart));

        return CartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!cartRepository.existsById(id)) {
            throw new CartNotFoundException(id);
        }
        cartRepository.deleteById(id);
    }

    private Cart findEntityOrThrow(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException(id));
    }
}