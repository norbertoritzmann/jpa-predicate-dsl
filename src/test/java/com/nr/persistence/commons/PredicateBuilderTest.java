package com.nr.persistence.commons;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.nr.persistence.commons.Conjunction;
import com.nr.persistence.commons.PredicateBuilder;
import com.nr.persistence.commons.util.FakeEntities;
import com.nr.persistence.query.dynamic.exception.PredicateBuilderBlockExpection;
import com.nr.persistence.test.EntityManagerTestContext;
import com.nr.persistence.test.EntityTestUser;

public class PredicateBuilderTest {
	private EntityManagerTestContext entityManagerContext;

	private EntityManager em;

	@Before
	public void setup() {
		entityManagerContext = new EntityManagerTestContext();

		em = entityManagerContext.start();
		FakeEntities.createFakeEntities(em);
		entityManagerContext.commitAndOpenTransaction();
	}

	@Test
	public void composedOperationTest() {
		Date start = Date.from(Instant.parse("2010-07-03T00:00:00.00Z"));
		Date end = Date.from(Instant.parse("2017-07-31T00:00:00.00Z"));
		String query = "er ";
		Boolean enabled = true;
		Double higherScore = 10.0;

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EntityTestUser> cq = cb.createQuery(EntityTestUser.class);
		Root<EntityTestUser> root = cq.from(EntityTestUser.class);

		PredicateBuilder<EntityTestUser> builder = new PredicateBuilder<>(root, cb, cq, EntityTestUser.class);
		builder.equal("enabled", enabled);
		builder.likeIgnoreCase("name", query);
		builder.lessOrEqualThan("score", higherScore);
		builder.between("since", start, end);

		Predicate predicate = builder.build();

		cq = cq.select(root).where(predicate);

		TypedQuery<EntityTestUser> q = em.createQuery(cq);
		List<EntityTestUser> users = q.getResultList();

		for (EntityTestUser entityTestUser : users) {
			MatcherAssert.assertThat(entityTestUser.getName(), Matchers.containsString(query));
			MatcherAssert.assertThat(entityTestUser.getSince().before(end), Matchers.is(true));
			MatcherAssert.assertThat(entityTestUser.getSince().after(start), Matchers.is(true));
			MatcherAssert.assertThat(entityTestUser.getEnabled(), Matchers.is(true));
			MatcherAssert.assertThat(entityTestUser.getScore(), Matchers.lessThan(10.0));
		}
	}

	@Test
	public void composedByBlocksTest() {
		Date start = Date.from(Instant.parse("2010-07-03T00:00:00.00Z"));
		Date end = Date.from(Instant.parse("2017-07-31T00:00:00.00Z"));
		String query = "er ";
		Boolean enabled = true;
		Double higherScore = 10.0;
		Integer higherNumberOfChildren = 3;
		Integer lowerNumberOfChildren = 1;

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EntityTestUser> cq = cb.createQuery(EntityTestUser.class);
		Root<EntityTestUser> root = cq.from(EntityTestUser.class);

		PredicateBuilder<EntityTestUser> builder = new PredicateBuilder<>(root, cb, cq, EntityTestUser.class);
		builder.equal("enabled", enabled)
				.likeIgnoreCase("name", query)
				.block()
				.lessOrEqualThan("score", higherScore)
				.between("numberOfChildren", lowerNumberOfChildren, higherNumberOfChildren)
				.between("since", start, end)
				.buildBlock(Conjunction.OR);

		Predicate predicate = builder.build();

		cq = cq.select(root).where(predicate);

		TypedQuery<EntityTestUser> q = em.createQuery(cq);
		List<EntityTestUser> users = q.getResultList();

		for (EntityTestUser u : users) {
			MatcherAssert.assertThat(u.getName(), Matchers.containsString(query));

			boolean orMatch = (u.getNumberOfChildren() >= lowerNumberOfChildren && u.getNumberOfChildren() >= higherNumberOfChildren) ||
					(u.getScore() <= higherScore) ||
					(u.getSince().before(end) && u.getSince().after(start));

			MatcherAssert.assertThat(orMatch, Matchers.is(true));
		}
	}

	@Test
	public void composedByOrBlockInStartPredicateTest() {
		Date start = Date.from(Instant.parse("2010-07-03T00:00:00.00Z"));
		Date end = Date.from(Instant.parse("2017-07-31T00:00:00.00Z"));
		String query = "er ";
		Boolean enabled = true;
		Double higherScore = 10.0;
		Integer higherNumberOfChildren = 3;
		Integer lowerNumberOfChildren = 1;

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EntityTestUser> cq = cb.createQuery(EntityTestUser.class);
		Root<EntityTestUser> root = cq.from(EntityTestUser.class);

		PredicateBuilder<EntityTestUser> builder = new PredicateBuilder<>(root, cb, cq, EntityTestUser.class);
		builder.equal("enabled", enabled)
				.block()
					.lessOrEqualThan("score", higherScore)
					.between("numberOfChildren", lowerNumberOfChildren, higherNumberOfChildren)
					.between("since", start, end)
				.buildBlock(Conjunction.OR)
				.likeIgnoreCase("name", query)
				.block()
					.lessOrEqualThan("score", higherScore)
					.between("numberOfChildren", lowerNumberOfChildren, higherNumberOfChildren)
					.between("since", start, end)
				.buildBlock(Conjunction.OR);

		Predicate predicate = builder.build();

		cq = cq.select(root).where(predicate);

		TypedQuery<EntityTestUser> q = em.createQuery(cq);
		List<EntityTestUser> users = q.getResultList();

		for (EntityTestUser u : users) {
			MatcherAssert.assertThat(u.getName(), Matchers.containsString(query));

			boolean orMatch = (u.getNumberOfChildren() >= lowerNumberOfChildren && u.getNumberOfChildren() >= higherNumberOfChildren) ||
					(u.getScore() <= higherScore) ||
					(u.getSince().before(end) && u.getSince().after(start));

			MatcherAssert.assertThat(orMatch, Matchers.is(true));
			MatcherAssert.assertThat(u.getEnabled(), Matchers.is(true));
		}
	}
	
	@Test(expected = PredicateBuilderBlockExpection.class)
	public void blockIncorrect() {
		Boolean enabled = true;
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EntityTestUser> cq = cb.createQuery(EntityTestUser.class);
		Root<EntityTestUser> root = cq.from(EntityTestUser.class);
		
		PredicateBuilder<EntityTestUser> builder = new PredicateBuilder<>(root, cb, cq, EntityTestUser.class);
		builder.equal("enabled", enabled)
			.buildBlock(Conjunction.OR);
	}
}
