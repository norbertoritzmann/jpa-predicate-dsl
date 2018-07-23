package com.nr.persistence.query.dynamic.exception;

public class MandatoryAnnotationProperty extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MandatoryAnnotationProperty(String message) {
		super(message);
	}
}
