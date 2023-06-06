package com.xMart.retailstorediscounts.exceptions;

public class NotFoundException extends RuntimeException {

	private String message;
	
	public NotFoundException(String message) {
		this.message = message;
	}
	
	public NotFoundException() {
	}
}
