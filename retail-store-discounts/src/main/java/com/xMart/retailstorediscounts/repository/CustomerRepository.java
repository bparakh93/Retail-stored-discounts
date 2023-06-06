package com.xMart.retailstorediscounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xMart.retailstorediscounts.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
