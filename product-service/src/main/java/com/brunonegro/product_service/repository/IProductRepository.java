package com.brunonegro.product_service.repository;

import com.brunonegro.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByCode(String code);
    List<Product> findAllByBrandAndCategory(String brand, String category);


}
