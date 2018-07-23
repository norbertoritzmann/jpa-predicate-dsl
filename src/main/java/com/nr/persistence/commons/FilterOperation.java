package com.nr.persistence.commons;

import com.nr.persistence.engine.PredicateMatching;

public enum FilterOperation {

	EQUALS {
		@Override
		public PredicateMatching getPredicateOperation() {
			return PredicateMatching.EQUALS;
		}
	},
	GREATER_THAN {
		@Override
		public PredicateMatching getPredicateOperation() {
			return PredicateMatching.GREATER;
		}
	},
	LESS_THAN {
		@Override
		public PredicateMatching getPredicateOperation() {
			return PredicateMatching.LESS;
		}
	},
	GREATER_OR_EQUALS {
		@Override
		public PredicateMatching getPredicateOperation() {
			return PredicateMatching.GREATER_OR_EQUALS;
		}
	},
	LESS_OR_EQUALS {
		@Override
		public PredicateMatching getPredicateOperation() {
			return PredicateMatching.LESS_OR_EQUALS;
		}
	},
	BETWEEN_START {
		@Override
		public PredicateMatching getPredicateOperation() {
			return PredicateMatching.BETWEEN;
		}
	},
	BETWEEN_END {
		@Override
		public PredicateMatching getPredicateOperation() {
			return PredicateMatching.BETWEEN;
		}
	},
	STARTS_WITH {
		@Override
		public PredicateMatching getPredicateOperation() {
			return PredicateMatching.STARTS_WITH;
		}
	},
	ENDS_WITH {
		@Override
		public PredicateMatching getPredicateOperation() {
			return PredicateMatching.ENDS_WITH;
		}
	},
	LIKE {
		@Override
		public PredicateMatching getPredicateOperation() {
			return PredicateMatching.LIKE;
		}
	};

	public abstract PredicateMatching getPredicateOperation();
}
