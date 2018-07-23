package com.nr.persistence.engine;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;

import org.springframework.util.ReflectionUtils;

public class PropertyAnnotationReader {

	private PropertyAnnotationReader() {}

	public static Annotation annotationBy(PropertyDescriptor propertyDescriptor, Class<? extends Annotation> annotationClass) {
		// Looks first in method
		Annotation annotationProperty = propertyDescriptor.getReadMethod().getAnnotation(annotationClass);
		if (annotationProperty == null) {
			annotationProperty =
					ReflectionUtils
					.findField(propertyDescriptor.getReadMethod().getDeclaringClass(), propertyDescriptor.getName())
					.getAnnotation(annotationClass);
		}

		return annotationProperty;
	}
}
