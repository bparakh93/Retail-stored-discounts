package com.xMart.retailstorediscounts.entity;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "customer")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotNull(message = "Name shouldn't be null")
	private String name;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name = "registration_date")
	private LocalDate registrationDate;

	@Column(name = "customer_type")
	@Enumerated(EnumType.STRING)
	private CustomerType customerType;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@Column(name = "mobile_number", unique = true)
	private Long mobileNumber;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
	}

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + id + ", name=" + name + ", registrationDate=" + registrationDate
				+ ", customerType=" + customerType + ", employee=" + employee + ", mobileNumber=" + mobileNumber + "]";
	}
	

	@Override
	public int hashCode() {
		return Objects.hash(customerType, employee, id, mobileNumber, name, registrationDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return customerType == other.customerType && Objects.equals(employee, other.employee) && id == other.id
				&& Objects.equals(mobileNumber, other.mobileNumber) && Objects.equals(name, other.name)
				&& Objects.equals(registrationDate, other.registrationDate);
	}

	public Customer(int id, String name, LocalDate registrationDate, CustomerType customerType,
			Employee employee, Long mobileNumber) {
		super();
		this.id = id;
		this.name = name;
		this.registrationDate = registrationDate;
		this.customerType = customerType;
		this.employee = employee;
		this.mobileNumber = mobileNumber;
	}

	public Customer() {
	}
}
