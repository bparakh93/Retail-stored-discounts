package com.xMart.retailstorediscounts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xMart.retailstorediscounts.entity.Product;
import com.xMart.retailstorediscounts.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	public ProductService productService;
	
	@PostMapping("/product")
	public ResponseEntity<Product> addProducts(@RequestBody Product product) {
		System.out.println("Req");
		return new ResponseEntity<>(productService.addProduct(product), HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<Product>> getAllProducts(){
		List<Product> products = productService.getAllProducts();
		return new ResponseEntity<>(products, HttpStatus.OK);
	}
}
