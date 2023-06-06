package com.xMart.retailstorediscounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xMart.retailstorediscounts.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

}
