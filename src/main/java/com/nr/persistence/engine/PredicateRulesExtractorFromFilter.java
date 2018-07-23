package com.nr.persistence.engine;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nr.persistence.commons.FilterField;
import com.nr.persistence.commons.FilterOperation;
import com.nr.persistence.commons.OrderBy;
import com.nr.persistence.commons.OrderByFields;
import com.nr.persistence.query.dynamic.exception.PairValuesNotFoundException;

public class PredicateRulesExtractorFromFilter<T> {

	private T filter;

	private EntityPropertyNameResolver entityPropertyNameResolver;

	public PredicateRulesExtractorFromFilter(T filter) {
		this.filter = filter;
		this.entityPropertyNameResolver = new EntityPropertyNameResolver();
	}

	public Collection<OrderBy> toOrderByFields(List<PropertyDescriptor> valuedProperties) {
		Collection<OrderBy> orders = new ArrayList<>();

		for (PropertyDescriptor descriptor : valuedProperties) {
			OrderByFields orderBy = (OrderByFields) PropertyAnnotationReader.annotationBy(descriptor, OrderByFields.class);
			if (orderBy != null) {
				Object genericObject = PropertyValueExtractor.valueBy(descriptor, filter);
				if (genericObject != null && Collection.class.isAssignableFrom(genericObject.getClass())) {
					orders = (Collection<OrderBy>) genericObject;
					break;
				}
			}
		}

		return orders;
	}

	public List<PredicateFilter> toPredicateFilters(List<FieldDescritor> fieldDescriptors) {
		List<PredicateFilter> predicateFilters = new ArrayList<>();
		Map<String, PropertyDescriptor> combinedProperties = new HashMap<>();

		for (FieldDescritor descritor : fieldDescriptors) {

			PredicateFilter predicateFilter = buildWithFilterField(descritor, combinedProperties);
			if (predicateFilter != null) {
				predicateFilters.add(predicateFilter);
			}
		}

		if (combinedProperties.size() > 0) {
			throw new PairValuesNotFoundException(filter.getClass());
		}

		return predicateFilters;
	}

	private PredicateFilter buildWithFilterField(FieldDescritor field, Map<String, PropertyDescriptor> combinedProperties) {
		PredicateFilter predicateFilter = new PredicateFilter();
		String entityProperty = entityPropertyNameResolver.getPropertyName(field.getPropertyDescriptor(), field.getFilterField());

		if (isCombinationDependentField(field.getFilterField())) {
			Boolean containsStart = combinedProperties.containsKey(encodeKey(entityProperty, FilterOperation.BETWEEN_START));
			Boolean contaisEnd = combinedProperties.containsKey(encodeKey(entityProperty, FilterOperation.BETWEEN_END));

			if (!containsStart && !contaisEnd) {
				combinedProperties.put(encodeKey(entityProperty, field.getFilterField().operation()), field.getPropertyDescriptor());
				return null;
			} else {
				combinedProperties.put(encodeKey(entityProperty, field.getFilterField().operation()), field.getPropertyDescriptor());

				// getFirstPropertyDescriptByOperation(FilterOperation.BETWEEN_START,
				PropertyDescriptor startField = combinedProperties.get(
						encodeKey(entityProperty, FilterOperation.BETWEEN_START));

				// getFirstPropertyDescriptByOperation(FilterOperation.BETWEEN_END,
				PropertyDescriptor endField = combinedProperties.get(
						encodeKey(entityProperty, FilterOperation.BETWEEN_END));

				predicateFilter = buildBetweenField(startField, endField, entityProperty);

				combinedProperties.remove(encodeKey(entityProperty, FilterOperation.BETWEEN_START));
				combinedProperties.remove(encodeKey(entityProperty, FilterOperation.BETWEEN_END));
			}

			return predicateFilter;
		} else if (field.getFilterField().operation() == null) {
			predicateFilter.setOperation(PredicateMatching.EQUALS);
		} else {
			predicateFilter.setOperation(field.getFilterField().operation().getPredicateOperation());
		}

		predicateFilter.setPropertyName(entityProperty);
		predicateFilter.setValue((Comparable<?>) PropertyValueExtractor.valueBy(field.getPropertyDescriptor(), filter));

		return predicateFilter;
	}

	private PredicateFilter buildBetweenField(PropertyDescriptor propertyDescriptorStartField,
			PropertyDescriptor propertyDescriptorEndField, String propertyName) {
		PredicateFilter predicateFilter = new PredicateFilter();
		predicateFilter.setStartValue((Comparable<?>) PropertyValueExtractor.valueBy(propertyDescriptorStartField, this.filter));
		predicateFilter.setEndValue((Comparable<?>) PropertyValueExtractor.valueBy(propertyDescriptorEndField, this.filter));
		predicateFilter.setOperation(PredicateMatching.BETWEEN);
		predicateFilter.setPropertyName(propertyName);

		return predicateFilter;
	}

	private boolean isCombinationDependentField(FilterField filterField) {

		switch (filterField.operation()) {
			case BETWEEN_END:
			case BETWEEN_START:
				return true;
			default:
				return false;
		}

	}

	private String encodeKey(String entityPropertyName, FilterOperation filterOperation) {
		return new StringBuilder(entityPropertyName).append(filterOperation).toString();
	}
}
