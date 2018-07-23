package com.nr.persistence.commons;

public class OrderBy {

	private String fieldName;
	private OrderByDirection direction = OrderByDirection.ASC;

	public OrderBy() {
	}

	public OrderBy(String fieldName, OrderByDirection direction) {
		super();
		this.fieldName = fieldName;
		this.direction = direction;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public OrderByDirection getDirection() {
		return direction;
	}

	public void setDirection(OrderByDirection direction) {
		this.direction = direction;
	}
}
