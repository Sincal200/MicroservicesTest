package com.productservice.product_service.repository;

import com.productservice.product_service.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
    // Additional query methods can be defined here if needed
}
