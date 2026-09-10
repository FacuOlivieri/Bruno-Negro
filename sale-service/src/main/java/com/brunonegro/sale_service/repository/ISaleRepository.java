package com.brunonegro.sale_service.repository;

import com.brunonegro.sale_service.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISaleRepository extends JpaRepository<Sale, Long> {

    //Busca el ultimo sale creado
    Optional<Sale> findFirstByOrderByIdSaleDesc();
}
