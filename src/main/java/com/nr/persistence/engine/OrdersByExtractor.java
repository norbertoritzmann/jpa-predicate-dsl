package com.nr.persistence.engine;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.nr.persistence.commons.OrderBy;
import com.nr.persistence.commons.PredicateBuilder;
import com.nr.persistence.commons.QueryBuilder;

public class OrdersByExtractor<E, F> {

	private QueryBuilder<E> builder;
	private PredicateRulesExtractorFromFilter<F> extractor;
	private F filter;

	public OrdersByExtractor(F filter, QueryBuilder<E> builder) {
		this.extractor = new PredicateRulesExtractorFromFilter<>(filter);
		this.builder = builder;
		this.filter = filter;
	}

	public PredicateBuilder<E> extract(List<String> exclude) {
		PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(filter.getClass());

		List<PropertyDescriptor> valuedProperties = new ArrayList<>();

		for (PropertyDescriptor propertyDescriptor : descriptors) {
			if (!exclude.contains(propertyDescriptor.getName())) {
				Object value = PropertyValueExtractor.valueBy(propertyDescriptor, filter);
				if (value != null) {
					valuedProperties.add(propertyDescriptor);
				}
			}
		}

		Collection<OrderBy> orders = extractor.toOrderByFields(valuedProperties);
		builder.orderBy(orders);

		return builder;
	}
	
}
