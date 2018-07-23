package com.nr.persistence.engine;

import static com.nr.persistence.engine.PropertyAnnotationReader.annotationBy;
import static com.nr.persistence.engine.PropertyValueExtractor.valueBy;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.nr.persistence.commons.FilterField;
import com.nr.persistence.commons.FilterOperation;
import com.nr.persistence.commons.PredicateBuilder;
import com.nr.persistence.query.dynamic.exception.MandatoryAnnotationProperty;

public class FilterPredicateExtractor<E, F> {

	private PredicateBuilder<E> builder;
	private PredicateRulesExtractorFromFilter<F> extractor;
	private F filter;

	public FilterPredicateExtractor(F filter, PredicateBuilder<E> builder) {
		this.extractor = new PredicateRulesExtractorFromFilter<>(filter);
		this.builder = builder;
		this.filter = filter;
	}

	public PredicateBuilder<E> extract(List<String> exclude) {
		PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(filter.getClass());
		List<FieldDescritor> valuedProperties = Arrays.stream(descriptors)
				.filter(desc -> !exclude.contains(desc.getName()))
				.filter(desc -> annotationBy(desc, FilterField.class) != null)
				.filter(desc -> valueBy(desc, filter) != null)
				.map(this::getFieldDescriptor)
				.collect(Collectors.toList());

		List<PredicateFilter> filters = extractor.toPredicateFilters(valuedProperties);
		filters.stream()
				.forEach(predicateFilter -> predicateFilter.getOperation().assignOperation(predicateFilter, builder));

		return builder;
	}

	private FieldDescritor getFieldDescriptor(PropertyDescriptor propertyDescriptor) {
		FilterField field = (FilterField) annotationBy(propertyDescriptor, FilterField.class);
		validateField(field, propertyDescriptor);

		return new FieldDescritor(propertyDescriptor, field);
	}

	private void validateField(FilterField field, PropertyDescriptor propertyDescriptor) {
		
		if ((FilterOperation.BETWEEN_START.equals(field.operation()) || FilterOperation.BETWEEN_END.equals(field.operation()))
				&& field.entityPropertyName().equals("")) {

			StringBuilder message = new StringBuilder("For the filter fields (");
			message.append(propertyDescriptor.getName()) 
					.append(") BETWEEN_START and BETWEEN_END is ")
					.append("mandatory to fill the annotation field: entityPropertyName");

			throw new MandatoryAnnotationProperty(message.toString());
		}
	}
}
