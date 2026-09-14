package com.brunonegro.sale_service.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSale;
    private LocalDate saleDate;

    private Long idCart;
    private Long IdClient;
    private double totalPrice;

    //orphanRemoval = true -> al reemplazar la lista, las lineas viejas se borran de la BD
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("detailIndex ASC")
    private List<SaleDetail> details = new ArrayList<>();

}