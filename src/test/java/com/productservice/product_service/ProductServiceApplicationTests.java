package com.productservice.product_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productservice.product_service.dto.ProductRequest;
import com.productservice.product_service.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception {
		String mutation = """
			mutation {
				createProduct(productRequest: {
					name: "iPhone 14"
					description: "Apple iPhone 14"
					price: 1200.0
				}) {
					id
					name
					description
					price
				}
			}
		""";

		String requestBody = buildGraphQLRequest(mutation);

		mockMvc.perform(MockMvcRequestBuilders.post("/graphql")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isOk());
	}

	@Test
	void shouldGetAllProducts() throws Exception {
		String query = """
			query {
				getAllProducts {
					id
					name
					description
					price
				}
			}
		""";

		String requestBody = buildGraphQLRequest(query);

		mockMvc.perform(MockMvcRequestBuilders.post("/graphql")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isOk());
        Assertions.assertEquals(1, productRepository.findAll().size());
	}

	private ProductRequest getProductRequest(){
		return ProductRequest.builder()
				.name("iPhone 14")
				.description("Apple iPhone 14")
				.price(BigDecimal.valueOf(1200))
				.build();
	}

	private String buildGraphQLRequest(String queryOrMutation) throws JsonProcessingException {
		return objectMapper.writeValueAsString(Map.of("query", queryOrMutation));
	}
}
