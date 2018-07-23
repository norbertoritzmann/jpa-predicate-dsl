package com.nr.persistence.engine;

public class PredicateFilter {

	private String propertyName;
	private PredicateMatching operation;
	private Comparable<?> value;
	private Comparable<?> startValue;
	private Comparable<?> endValue;

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public PredicateMatching getOperation() {
		return operation;
	}

	public void setOperation(PredicateMatching operation) {
		this.operation = operation;
	}

	public Comparable<?> getValue() {
		return value;
	}

	public void setValue(Comparable<?> value) {
		this.value = value;
	}

	public Comparable<?> getStartValue() {
		return startValue;
	}

	public void setStartValue(Comparable<?> startValue) {
		this.startValue = startValue;
	}

	public Comparable<?> getEndValue() {
		return endValue;
	}

	public void setEndValue(Comparable<?> endValue) {
		this.endValue = endValue;
	}

}
