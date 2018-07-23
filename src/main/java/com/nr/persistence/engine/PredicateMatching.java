package com.nr.persistence.engine;

import com.nr.persistence.commons.PredicateBuilder;

public enum PredicateMatching {

	EQUALS {
		@Override
		public <T> void assignOperation(PredicateFilter filter, PredicateBuilder<T> predicate) {
			predicate.equal(filter.getPropertyName(), (Comparable<?>) filter.getValue());
		}
	},
	GREATER {
		@Override
		public <T> void assignOperation(PredicateFilter filter, PredicateBuilder<T> predicate) {
			predicate.greaterThan(filter.getPropertyName(), (Comparable<?>) filter.getValue());
		}
	},
	LESS {
		@Override
		public <T> void assignOperation(PredicateFilter filter, PredicateBuilder<T> predicate) {
			predicate.lessThan(filter.getPropertyName(), (Comparable<?>) filter.getValue());
		}
	},
	GREATER_OR_EQUALS {
		@Override
		public <T> void assignOperation(PredicateFilter filter, PredicateBuilder<T> predicate) {
			predicate.greaterOrEqualThan(filter.getPropertyName(), (Comparable<?>) filter.getValue());
		}
	},
	LESS_OR_EQUALS {
		@Override
		public <T> void assignOperation(PredicateFilter filter, PredicateBuilder<T> predicate) {
			predicate.lessOrEqualThan(filter.getPropertyName(), (Comparable<?>) filter.getValue());
		}
	},
	BETWEEN {
		@Override
		public <T> void assignOperation(PredicateFilter filter, PredicateBuilder<T> predicate) {
			predicate.between(filter.getPropertyName(), filter.getStartValue(), filter.getEndValue());
		}
	},
	STARTS_WITH {
		@Override
		public <T> void assignOperation(PredicateFilter filter, PredicateBuilder<T> predicate) {
			predicate.likeStartsWith(filter.getPropertyName(), (String) filter.getValue());
		}
	},
	ENDS_WITH {
		@Override
		public <T> void assignOperation(PredicateFilter filter, PredicateBuilder<T> predicate) {
			predicate.likeEndsWith(filter.getPropertyName(), (String) filter.getValue());
		}
	},
	LIKE {
		@Override
		public <T> void assignOperation(PredicateFilter filter, PredicateBuilder<T> predicate) {
			predicate.likeIgnoreCase(filter.getPropertyName(), (String) filter.getValue());
		}
	};

	public abstract <T> void assignOperation(PredicateFilter filter, PredicateBuilder<T> predicate);
}
