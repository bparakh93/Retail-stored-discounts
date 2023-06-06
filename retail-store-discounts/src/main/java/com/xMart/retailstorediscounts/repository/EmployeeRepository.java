package com.xMart.retailstorediscounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xMart.retailstorediscounts.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>{

}
