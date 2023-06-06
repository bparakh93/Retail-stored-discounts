package com.xMart.retailstorediscounts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xMart.retailstorediscounts.entity.Customer;
import com.xMart.retailstorediscounts.entity.CustomerType;
import com.xMart.retailstorediscounts.service.CustomerService;
import com.xMart.retailstorediscounts.service.EmployeeService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	@Autowired
	private EmployeeService employeeService;

	@PostMapping("/customer")
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
		System.out.println("Received req");
		System.out.println(customer);
		if ((!customer.getCustomerType().equals(CustomerType.EMPLOYEE) && customer.getEmployee() != null)
				|| (customer.getEmployee() != null
						&& employeeService.retrieveEmployee(customer.getEmployee().getId()).isPresent()))
			customer.setEmployee(null);
		return new ResponseEntity<>(customerService.createNewCustomer(customer), HttpStatus.CREATED);
	}

	@GetMapping("/customer/{id}")
	public ResponseEntity<Customer> fetchCustomer(@PathVariable Integer id) {
		return new ResponseEntity<>(customerService.fetchCustomer(id), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<Customer>> getAllCustomers() {
		List<Customer> customers = customerService.fetchAllCustomers();
		return new ResponseEntity<>(customers, HttpStatus.OK);
	}

	@DeleteMapping("customer/{id}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
		customerService.deleteCustomer(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
