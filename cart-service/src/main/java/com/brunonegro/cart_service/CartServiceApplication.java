package com.brunonegro.cart_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// TODO: integracion con product-service.
// Descomentar @EnableFeignClients (y su import) cuando crees el cliente Feign
// hacia product-service. Recorda agregar tambien spring-cloud-starter-loadbalancer
// al pom.xml, que es lo que le permite a Feign resolver el servicio por nombre.
// import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
// @EnableFeignClients
public class CartServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartServiceApplication.class, args);
	}

}