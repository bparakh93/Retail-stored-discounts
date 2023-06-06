package com.xMart.retailstorediscounts.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	private boolean isActive;

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

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Employee(int id, String name, boolean isActive) {
		super();
		this.id = id;
		this.name = name;
		this.isActive = isActive;
	}

	public Employee() {
	}

	@Override
	public String toString() {
		return "Employee [empId=" + id + ", name=" + name + ", IsActive=" + isActive + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, isActive, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return id == other.id && isActive == other.isActive && Objects.equals(name, other.name);
	}
}
