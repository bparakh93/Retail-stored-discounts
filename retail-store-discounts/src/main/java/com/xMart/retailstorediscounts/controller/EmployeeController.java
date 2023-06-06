package com.xMart.retailstorediscounts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xMart.retailstorediscounts.entity.Employee;
import com.xMart.retailstorediscounts.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@PostMapping("/employee")
	public Employee createEmployee(@RequestBody Employee employee) {
		System.out.println(employee);
		return employeeService.createEmployee(employee);
	}

}
