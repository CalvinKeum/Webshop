package com.calvinkeum.productservice.repository;

import com.calvinkeum.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}