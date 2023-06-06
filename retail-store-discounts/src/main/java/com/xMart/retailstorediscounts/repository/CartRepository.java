package com.xMart.retailstorediscounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xMart.retailstorediscounts.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer>{

}
