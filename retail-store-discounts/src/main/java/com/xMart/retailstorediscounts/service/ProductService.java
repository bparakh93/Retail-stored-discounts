package com.xMart.retailstorediscounts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xMart.retailstorediscounts.entity.Product;
import com.xMart.retailstorediscounts.exceptions.NotFoundException;
import com.xMart.retailstorediscounts.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public Product addProduct(Product product) {
		return productRepository.save(product);
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Product getProduct(Integer productId) {
		return productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
	}
}
