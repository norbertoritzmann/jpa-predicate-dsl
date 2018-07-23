package com.nr.persistence.engine;

import java.beans.PropertyDescriptor;

import com.nr.persistence.commons.FilterField;

public class FieldDescritor {

	private PropertyDescriptor propertyDescriptor;

	private FilterField filterField;

	public FieldDescritor() {

	}

	public FieldDescritor(PropertyDescriptor propertyDescriptor, FilterField filterField) {
		super();
		this.propertyDescriptor = propertyDescriptor;
		this.filterField = filterField;
	}

	public PropertyDescriptor getPropertyDescriptor() {
		return propertyDescriptor;
	}

	public void setPropertyDescriptor(PropertyDescriptor propertyDescriptor) {
		this.propertyDescriptor = propertyDescriptor;
	}

	public FilterField getFilterField() {
		return filterField;
	}

	public void setFilterField(FilterField filterField) {
		this.filterField = filterField;
	}

}
