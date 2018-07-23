package com.nr.persistence.test;

import java.util.Collection;
import java.util.Date;

import com.nr.persistence.commons.FilterField;
import com.nr.persistence.commons.FilterOperation;
import com.nr.persistence.commons.OrderBy;
import com.nr.persistence.commons.OrderByFields;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterTestUser {

	@FilterField(operation = FilterOperation.LIKE)
	private String name;

	@FilterField(entityPropertyName = "department.name", operation = FilterOperation.STARTS_WITH)
	private String departmentName;

	@FilterField(entityPropertyName = "name", operation = FilterOperation.ENDS_WITH)
	private String nameSufix;

	@FilterField(entityPropertyName = "department.location.name", operation = FilterOperation.STARTS_WITH)
	private String departmentLocationName;

	@FilterField(entityPropertyName = "since", operation = FilterOperation.BETWEEN_START)
	private Date sinceStart;

	@FilterField(entityPropertyName = "since", operation = FilterOperation.BETWEEN_END)
	private Date sinceEnd;

	@FilterField(operation = FilterOperation.EQUALS)
	private Boolean enabled;

	@FilterField(operation = FilterOperation.EQUALS)
	private Integer numberOfChildren;

	@FilterField(entityPropertyName = "score", operation = FilterOperation.LESS_THAN)
	private Double higherScore;

	@FilterField(entityPropertyName = "since", operation = FilterOperation.LESS_OR_EQUALS)
	private Date beforeThan;
	
	@FilterField(entityPropertyName = "since", operation = FilterOperation.GREATER_OR_EQUALS)
	private Date afterThan;

	@FilterField(entityPropertyName = "score", operation = FilterOperation.GREATER_THAN)
	private Double lowerScore;

	@OrderByFields(availableEntityFieldsForSorting = {"since", "name", "score"})
	private Collection<OrderBy> orderBy;
}
