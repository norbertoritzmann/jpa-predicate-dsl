package com.nr.persistence.commons;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotation used to create dynamic filters.
 * Create a filter class and every property filled anotated with @FilterField will be processed
 * as condition. Build the PredicateBuilder like this:
 * 
 * <p>
 *    <pre>
 *      PredicateFilterRulesAccessor< YourEntity, YourFilter > accessor = new PredicateFilterRulesAccessor<>(
 *				filter, YourEntity.class, root, (CriteriaQuery< YourEntity >) query, cb);
 *		PredicateBuilder< YourEntity > builder = accessor.access();
 *
 *		//You can add extra predicates to the builder
 *		builder.between(DEADLINE, startingDay, endingDay);
 *
 *		//And create the javax.persistence.criteria.Predicate
 *		javax.persistence.criteria.Predicate predicate = builder.build();
 *	  </pre>
 * </p>
 * 
 * <p>To get the javax.persistence.criteria.Predicate just use the method build() of the builder.</p>
 * 
 * @see com.nr.persistence.commons.PredicateFilterRulesAccessor
 * @see com.nr.persistence.commons.PredicateBuilder
 *
 **/
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface FilterField {

	/**
	 * Entity reference property name that filter is working on. Could be omitted if 
	 * the filter property name is exacly the same of the entity.
	 * Suports full property path for complex name path like:
	 * <p><pre>product.location.name</pre></p>
	 **/
	String entityPropertyName() default "";

	/**
	 * Use to declare witch operation will be used in the predicate:
	 * EQUALS (Default), LIKE, STARTS_WITH, GREATER_THAN...
	 **/
	FilterOperation operation() default FilterOperation.EQUALS;

}
