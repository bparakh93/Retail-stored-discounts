package com.xMart.retailstorediscounts.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xMart.retailstorediscounts.entity.Cart;
import com.xMart.retailstorediscounts.entity.CartItems;
import com.xMart.retailstorediscounts.entity.Customer;
import com.xMart.retailstorediscounts.entity.CustomerType;
import com.xMart.retailstorediscounts.entity.Product;
import com.xMart.retailstorediscounts.exceptions.NotFoundException;
import com.xMart.retailstorediscounts.repository.CartItemsRepository;
import com.xMart.retailstorediscounts.repository.CartRepository;

@Service
public class CartService {

	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CartItemsRepository cartItemsRepository;
	@Autowired
	private ProductService productService;
	@Autowired
	private CustomerService customerService;

	public Cart createNewCart(Integer customerId) {
		Customer customer = customerService.fetchCustomer(customerId);
		Cart cart = new Cart();
		cart.setCustomer(customer);
		cart.setOrderDateTime(LocalDateTime.now());
		return cartRepository.save(cart);
	}

	public Cart findById(Integer cartId) {
		return cartRepository.findById(cartId).orElseThrow(() -> new NotFoundException("Cart not found"));
	}

	public void add(Optional<Cart> cart, Product product) {

	}

	public Cart addProductToCart(Integer cartId, Integer productId, int quantity) {
		Cart cart = findById(cartId);
		Product product = productService.getProduct(productId);
		CartItems cartItem = new CartItems();
		cartItem.setCart(cart);
		cartItem.setProduct(product);
		cartItem.setQuantity(quantity);
		cartItemsRepository.save(cartItem);
		cart.setCartTotal(getCartTotal(cartId));
		return cartRepository.save(cart);
	}

	public Cart getCartDetails(Integer id) {
		Optional<Cart> cart = cartRepository.findById(id);
		return cart.orElseThrow(() -> new NotFoundException("Cart Not found"));
	}

	public Double getCartTotal(Integer cartId) {
		Cart cart = getCartDetails(cartId);
		return cart.getCartItems().stream().map((cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity()))
				.reduce((double) 0, ((a, b) -> a + b));
	}

	public void deleteCart(Integer cartId) {
		cartRepository.deleteById(cartId);
	}

	public Double getNetPayable(Integer cartId) {
		Cart cart = getCartDetails(cartId);
		Customer customer = cart.getCustomer();
		Double cartTotal = cart.getCartTotal();
		Double groceriesTotal = cart.getCartItems().stream()
				.filter((cartItem) -> ("Groceries".equals(cartItem.getProduct().getCategory())))
				.map((cartItem) -> cartItem.getProduct().getPrice() * cartItem.getQuantity())
				.reduce((double) 0, ((a, b) -> a + b));
		double standardDiscount = ((int)(cartTotal/100))*5;
		if (customer.getCustomerType().equals(CustomerType.EMPLOYEE) && customer.getEmployee() != null
				&& customer.getEmployee().getIsActive())
			return cartTotal - standardDiscount - (((cartTotal - groceriesTotal) * 30) / 100);
		else if (customer.getCustomerType().equals(CustomerType.AFFILIATE))
			return cartTotal - standardDiscount - (((cartTotal - groceriesTotal) * 10) / 100);
		else if (customer.getRegistrationDate().isBefore(LocalDate.now().minusYears(2)))
			return cartTotal - standardDiscount - (((cartTotal - groceriesTotal) * 5) / 100);
		else 
			return cartTotal - standardDiscount;
		
	}

}
