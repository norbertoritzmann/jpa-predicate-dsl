package com.nr.persistence.query.dynamic.exception;

public class GetMethodNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GetMethodNotFoundException(String propertyName) {
		super(String.format("Get method not found for the property %s", propertyName));
	}
}
