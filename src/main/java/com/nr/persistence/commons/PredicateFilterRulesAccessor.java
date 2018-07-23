package com.nr.persistence.commons;

import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.nr.persistence.engine.FilterPredicateExtractor;

/**
 * Accessor to rules implemented in business filter class. This class extract this rules and
 * generates a PredicateBuilder. This rules are written by annotations like
 * <code>@FilterField</code> and <code>@OrderBy</code>
 * 
 * @see com.nr.persistence.commons.FilterField
 *		com.softexpert.persistence.commons.OrderBy
 *      com.softexpert.persistence.commons.PredicateBuilder
 * 
 * @author norberto.ritzmann
 **/
public final class PredicateFilterRulesAccessor<E, F> {

	private PredicateBuilder<E> builder;

	private F filter;

	private List<String> notReadableProperties = Arrays.asList("class");

	private Conjunction mergeType;

	public PredicateFilterRulesAccessor(F filter, Class<E> entityClass, Root<E> root, CriteriaQuery<E> query, CriteriaBuilder cb) {
		this(filter, entityClass, root, query, cb, Conjunction.AND);
	}

	public PredicateFilterRulesAccessor(F filter, Class<E> entityClass, Root<E> root, CriteriaQuery<E> query, CriteriaBuilder cb,
			Conjunction mergeType) {
		this.filter = filter;
		this.builder = new PredicateBuilder<>(root, cb, query, entityClass);
		this.mergeType = mergeType;
	}

	public Predicate getPredicate() {
		return access().build(this.mergeType);
	}

	public PredicateBuilder<E> access() {
		FilterPredicateExtractor<E, F> predicateExtractor = new FilterPredicateExtractor<>(filter, builder);

		return predicateExtractor.extract(notReadableProperties);
	}
}
