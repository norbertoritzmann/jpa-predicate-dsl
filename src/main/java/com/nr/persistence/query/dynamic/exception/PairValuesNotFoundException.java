package com.nr.persistence.query.dynamic.exception;

public class PairValuesNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PairValuesNotFoundException(Class<?> clazz) {
		super("Some field(s) require(s) " + clazz.getCanonicalName() + " a pair value and may not have been filled");
	}
}
