package com.nr.persistence.engine;

import java.beans.PropertyDescriptor;

import com.nr.persistence.commons.FilterField;

public class EntityPropertyNameResolver {

	public static String getPropertyName(PropertyDescriptor propertyDescriptor, FilterField field) {
		if (field != null && !"".equals(field.entityPropertyName())) {
			return field.entityPropertyName();
		}

		return propertyDescriptor.getName();
	}
}
