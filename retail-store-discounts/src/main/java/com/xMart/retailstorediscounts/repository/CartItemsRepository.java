package com.xMart.retailstorediscounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xMart.retailstorediscounts.entity.CartItems;

public interface CartItemsRepository extends JpaRepository<CartItems, Integer>{

}
