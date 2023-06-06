package com.xMart.retailstorediscounts.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xMart.retailstorediscounts.entity.Employee;
import com.xMart.retailstorediscounts.repository.EmployeeRepository;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;

	public Employee createEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}
	
	public Optional<Employee> retrieveEmployee(int employeeId) {
		return employeeRepository.findById(employeeId);
	}

}
