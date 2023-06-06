package com.xMart.retailstorediscounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "product")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(name = "product_name")
	private String productName;
	@Column(name = "product_category")
	private String category;
	private Double price;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Product(int id, String productName, String category, Double price) {
		super();
		this.id = id;
		this.productName = productName;
		this.category = category;
		this.price = price;
	}

	public Product() {
	}

	@Override
	public String toString() {
		return "Product [productId=" + id + ", productName=" + productName + ", category=" + category
				+ ", price=" + price + "]";
	}
}
