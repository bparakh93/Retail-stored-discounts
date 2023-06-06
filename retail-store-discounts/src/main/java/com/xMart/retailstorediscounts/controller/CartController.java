package com.xMart.retailstorediscounts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xMart.retailstorediscounts.entity.Cart;
import com.xMart.retailstorediscounts.service.CartService;

@RestController
@RequestMapping("/carts")
public class CartController {

	@Autowired
	public CartService cartService;

	@PostMapping("/cart")
	public ResponseEntity<Cart> createCart(@RequestParam Integer customerId) {
		return new ResponseEntity<>(cartService.createNewCart(customerId), HttpStatus.CREATED);
	}

	@PostMapping("/{cartId}/products")
	public ResponseEntity<Cart> addProductToCart(@PathVariable Integer cartId, @RequestParam Integer productId,
			@RequestParam int quantity) {
		Cart cart = cartService.addProductToCart(cartId, productId, quantity);
		cart.setCartTotal(cartService.getCartTotal(cartId));
		return new ResponseEntity<>(cart, HttpStatus.OK);
	}

	@GetMapping("/cart/{cartId}/total")
	public ResponseEntity<Double> getCartTotal(@PathVariable Integer cartId) {
		Double total = cartService.getCartTotal(cartId);
		return new ResponseEntity<>(total, HttpStatus.OK);
	}

	@GetMapping("/cart/{cartId}")
	public ResponseEntity<Cart> fetchCustomer(@PathVariable Integer cartId) {
		return new ResponseEntity<>(cartService.getCartDetails(cartId), HttpStatus.OK);
	}
	
	@DeleteMapping("/cart/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Integer cartId) {
        cartService.deleteCart(cartId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
	
	@GetMapping("/cart/{cartId}/netPayable")
	public ResponseEntity<Double> getNetPayableAfterDiscountsPolicy(@PathVariable Integer cartId){
		Double netPayable = cartService.getNetPayable(cartId);
		return new ResponseEntity<>(netPayable, HttpStatus.OK);
	}
}
