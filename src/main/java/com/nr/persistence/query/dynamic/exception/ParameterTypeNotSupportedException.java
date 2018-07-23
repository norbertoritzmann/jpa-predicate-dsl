package com.nr.persistence.query.dynamic.exception;

public class ParameterTypeNotSupportedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ParameterTypeNotSupportedException(Class<?> parameterClass) {
		super(String.format("Parameter class %s is not supported", parameterClass.getCanonicalName()));
	}
}
