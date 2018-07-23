package com.nr.persistence.engine;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import com.nr.persistence.query.dynamic.exception.GetMethodNotFoundException;

public final class PropertyValueExtractor {

	private PropertyValueExtractor() {}

	public static <F> Object valueBy(PropertyDescriptor desc, F filter) {
		try {

			return desc.getReadMethod().invoke(filter);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

			throw new GetMethodNotFoundException(desc.getName());
		}
	}
}
