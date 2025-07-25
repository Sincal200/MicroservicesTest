package com.productservice.product_service.service;

import com.productservice.product_service.dto.ProductRequest;
import com.productservice.product_service.dto.ProductResponse;
import com.productservice.product_service.model.Product;
import com.productservice.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {


    private final ProductRepository productRepository;


    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price((productRequest.getPrice())) // Convert String to BigDecimal
                .build();
        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
        return mapToProductResponse(product);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products  = productRepository.findAll();

        return products.stream().map(this::mapToProductResponse).toList();

    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
