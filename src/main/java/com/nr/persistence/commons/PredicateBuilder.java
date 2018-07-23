package com.nr.persistence.commons;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.util.StringUtils;

import com.nr.persistence.query.dynamic.exception.PredicateBuilderBlockExpection;

/**
 * Encapsulates the complexity to build predicates in JPA. Could be used in a Specification using
 * through PredicateFilterRulesAccessor, or through DynamicQuery (use DynamicQueryFactory to create
 * instances) or by it self.
 * 
 * @see com.softexpert.persistence.commons.PredicateFilterRulesAccessor
 * 
 * @author norberto.ritzmann
 **/
public class PredicateBuilder<E> {

	protected CriteriaBuilder cb;
	protected CriteriaQuery<E> query;
	protected Root<E> root;
	protected List<Predicate> predicates;
	protected Class<E> entityClass;
	private PredicateBuilder<E> parent;

	public PredicateBuilder(CriteriaBuilder cb, Class<E> entityClass) {
		this(cb, entityClass, null);
	}

	public PredicateBuilder(CriteriaBuilder cb, Class<E> entityClass, PredicateBuilder<E> parent) {
		this.cb = cb;
		this.predicates = new ArrayList<>();
		this.query = cb.createQuery(entityClass);
		this.root = query.from(entityClass);
		this.entityClass = entityClass;
		this.parent = parent;
	}

	public PredicateBuilder(Root<E> root, CriteriaBuilder cb, CriteriaQuery<E> query, Class<E> entityClass) {
		this(root, cb, query, entityClass, null);
	}

	public PredicateBuilder(Root<E> root, CriteriaBuilder cb, CriteriaQuery<E> query, Class<E> entityClass, PredicateBuilder<E> parent) {
		this.cb = cb;
		this.predicates = new ArrayList<>();
		this.query = query;
		this.root = root;
		this.entityClass = entityClass;
		this.parent = parent;
	}

	/**
	 * Add a sub-block to add different conjunction like:
	 * 
	 * <p>
	 * 
	 * <pre>
	 * builder.greaterOrEqualThan("user.age", 18)
	 * 		.block()
	 * 			.equal("movie.genre", "horror")
	 * 			.equal("movie.kind", "violent")
	 * 			.buildBlock(Conjunction.OR)
	 * 		.build(Conjunction.AND);
	 * </pre>
	 * </p>
	 * 
	 * <p>
	 * The resultant predicate is:
	 * </p>
	 * 
	 * <p>
	 * user.age >= 18 and (movie.genre = 'horror' or movie.kind = 'violent')
	 * </p>
	 * 
	 * @return PredicateBuilder&#60;E&#62; builder instance
	 **/
	public PredicateBuilder<E> block() {
		return new PredicateBuilder<>(root, cb, query, this.entityClass, this);
	}

	/**
	 * After declaring the block() and adding some predicates is mandatory to buildBlock before
	 * execute the build() method. See more details in doc of block() method.
	 * 
	 * @param Conjunction Conjunction.AND or Conjunction.OR
	 * @return PredicateBuilder&#60;E&#62 builder instance
	 **/
	public PredicateBuilder<E> buildBlock(Conjunction type) {
		if (parent == null) {
			throw new PredicateBuilderBlockExpection("This instance is not block.");
		}

		if (this.predicates.isEmpty()) {
			return parent;
		}

		Predicate p = build(type);
		parent.add(p);
		return parent;
	}

	public PredicateBuilder<E> between(String field, Object start, Object end) {
		if (start instanceof Date) {
			return between(field, (Date) start, (Date) end);
		}
		if (start instanceof Integer) {
			return between(field, (Integer) start, (Integer) end);
		}
		if (start instanceof Double) {
			return between(field, (Double) start, (Double) end);
		}
		if (start instanceof Long) {
			return between(field, (Long) start, (Long) end);
		}
		if (start instanceof ZonedDateTime) {
			return between(field, (ZonedDateTime) start, (ZonedDateTime) end);
		}
		if (start instanceof OffsetDateTime) {
			return between(field, (OffsetDateTime) start, (OffsetDateTime) end);
		}
		if (start instanceof LocalDateTime) {
			return between(field, (LocalDateTime) start, (LocalDateTime) end);
		}
		if (start instanceof LocalDate) {
			return between(field, (LocalDate) start, (LocalDate) end);
		}
		if (start instanceof LocalTime) {
			return between(field, (LocalTime) start, (LocalTime) end);
		}

		return null;
	}

	public <Y extends Comparable<? super Y>> PredicateBuilder<E> between(String field, Y start, Y end) {
		Predicate predicate = cb.between(solvePath(field), start, end);
		predicates.add(predicate);

		return this;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public PredicateBuilder<E> greaterThan(String field, Comparable value) {
		Predicate predicate = cb.greaterThan(solvePath(field), value);
		predicates.add(predicate);

		return this;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public PredicateBuilder<E> greaterOrEqualThan(String field, Comparable value) {
		Predicate predicate = cb.greaterThanOrEqualTo(solvePath(field), value);
		predicates.add(predicate);

		return this;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public PredicateBuilder<E> lessThan(String field, Comparable value) {
		Predicate predicate = cb.lessThan(solvePath(field), value);
		predicates.add(predicate);

		return this;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public PredicateBuilder<E> lessOrEqualThan(String field, Comparable value) {
		Predicate predicate = cb.lessThanOrEqualTo(solvePath(field), value);
		predicates.add(predicate);

		return this;
	}

	public PredicateBuilder<E> equal(String field, Object value) {
		Predicate predicate = cb.equal(solvePath(field), value);
		predicates.add(predicate);

		return this;
	}

	public PredicateBuilder<E> likeIgnoreCase(String field, String value) {
		Predicate predicate = cb.like(cb.upper(solvePath(field)), wrappupForLikeExpression(value.toUpperCase()));
		predicates.add(predicate);

		return this;
	}

	public PredicateBuilder<E> likeEndsWith(String field, String value) {
		Predicate predicate = cb.like(solvePath(field), wrappupForEndsWithLikeExpression(value));
		predicates.add(predicate);

		return this;
	}

	public PredicateBuilder<E> likeStartsWith(String field, String value) {
		Predicate predicate = cb.like(solvePath(field), wrappupForStartsWithLikeExpression(value));
		predicates.add(predicate);

		return this;
	}

	public PredicateBuilder<E> add(Predicate predicate) {
		if (predicate != null) {
			predicates.add(predicate);
		}

		return this;
	}

	protected Predicate join(Conjunction type, Predicate[] predicates) {
		return type.equals(Conjunction.AND) ? cb.and(predicates) : cb.or(predicates);
	}

	public Predicate build(Conjunction type) {
		return join(type, toArray());
	}

	public Predicate build() {
		return build(Conjunction.AND);
	}

	protected Predicate[] toArray() {
		return predicates.toArray(new Predicate[0]);
	}

	protected <T> Path<T> solvePath(String field) {
		List<String> tokens = Arrays.asList(StringUtils.tokenizeToStringArray(field, "."));

		if (tokens.size() == 1) {

			return root.get(field);
		} else {
			Iterator<String> fieldIterator = tokens.iterator();
			Join<Object, Object> join = root.join(fieldIterator.next(), JoinType.LEFT);

			int numberOfJoins = tokens.size() - 1;
			int currentJoin = 2;

			while (numberOfJoins == currentJoin) {
				join = join.join(fieldIterator.next(), JoinType.LEFT);
				currentJoin++;
			}

			return join.get(fieldIterator.next());
		}
	}

	protected Order convertOrderBy(OrderBy orderBy) {
		return new OrderImpl(root.get(orderBy.getFieldName()), (orderBy.getDirection() == OrderByDirection.ASC ? true : false));
	}

	private String wrappupForLikeExpression(String value) {
		StringBuilder builder = new StringBuilder();
		return builder.append("%").append(value).append("%").toString();
	}

	private String wrappupForStartsWithLikeExpression(String value) {
		StringBuilder builder = new StringBuilder();
		return builder.append(value).append("%").toString();
	}

	private String wrappupForEndsWithLikeExpression(String value) {
		StringBuilder builder = new StringBuilder();
		return builder.append("%").append(value).toString();
	}
}
