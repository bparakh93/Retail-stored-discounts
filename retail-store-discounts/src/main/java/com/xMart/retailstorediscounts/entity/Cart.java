package com.xMart.retailstorediscounts.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart")
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cartId;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
	@JsonManagedReference
	private List<CartItems> cartItems;

	@Column(name = "order_datetime")
	private LocalDateTime orderDateTime;

	@Column(name = "cart_total")
	private Double cartTotal;

	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<CartItems> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItems> cartItems) {
		this.cartItems = cartItems;
	}

	public LocalDateTime getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(LocalDateTime orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public Double getCartTotal() {
		return cartTotal;
	}

	public void setCartTotal(Double cartTotal) {
		this.cartTotal = cartTotal;
	}

	@Override
	public String toString() {
		return "Cart [cartId=" + cartId + ", customerId=" + customer + ", orderDateTime=" + orderDateTime
				+ ", cartTotal=" + cartTotal + "]";
	}

	public Cart(int cartId, Customer customer, List<Product> products, LocalDateTime orderDateTime, Double cartTotal) {
		super();
		this.cartId = cartId;
		this.customer = customer;
		this.orderDateTime = orderDateTime;
		this.cartTotal = cartTotal;
	}

	public Cart() {

	}
}