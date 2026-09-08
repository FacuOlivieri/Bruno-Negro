package com.brunonegro.cart_service.repository;

import com.brunonegro.cart_service.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByIdUser(Long idUser);
}