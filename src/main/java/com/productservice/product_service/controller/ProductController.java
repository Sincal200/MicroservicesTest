package com.productservice.product_service.controller;

import com.productservice.product_service.dto.ProductRequest;
import com.productservice.product_service.dto.ProductResponse;
import com.productservice.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @MutationMapping
    public ProductResponse createProduct(@Argument("productRequest") ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    @QueryMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }
}
