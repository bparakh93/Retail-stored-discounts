package com.xMart.retailstorediscounts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xMart.retailstorediscounts.entity.Customer;
import com.xMart.retailstorediscounts.entity.CustomerType;
import com.xMart.retailstorediscounts.exceptions.NotFoundException;
import com.xMart.retailstorediscounts.repository.CustomerRepository;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;

	public Customer createNewCustomer(Customer customer) {
		return customerRepository.save(customer);
	}
	
	public boolean checkAffiliate(Integer id) {
		Optional<Customer> cust = customerRepository.findById(id);
		return cust.get().getCustomerType().equals(CustomerType.AFFILIATE);
	}

	public Customer fetchCustomer(Integer id) {
		return customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer not found"));
	}

	public List<Customer> fetchAllCustomers() {
		return customerRepository.findAll();
	}

	public void deleteCustomer(Integer id) {
		customerRepository.deleteById(id);
	}
}
