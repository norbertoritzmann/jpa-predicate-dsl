package com.nr.persistence.engine;

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

import com.nr.persistence.commons.PredicateBuilder;
import com.nr.persistence.commons.PredicateFilterRulesAccessor;
import com.nr.persistence.commons.util.FakeEntities;
import com.nr.persistence.query.dynamic.exception.PairValuesNotFoundException;
import com.nr.persistence.test.EntityManagerTestContext;
import com.nr.persistence.test.EntityTestUser;
import com.nr.persistence.test.FilterTestUser;

public class FilterPredicateExtractorTest {
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
	public void autoDetenctionFilterFieldsTest() {
		Date start = Date.from(Instant.parse("2010-07-03T00:00:00.00Z"));
		Date end = Date.from(Instant.parse("2017-07-31T00:00:00.00Z"));
		String query = "st";

		FilterTestUser filter = new FilterTestUser();
		filter.setEnabled(true);
		filter.setHigherScore(10.0);
		filter.setAfterThan(start);
		filter.setBeforeThan(end);
		filter.setNameSufix(query);

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EntityTestUser> cq = cb.createQuery(EntityTestUser.class);
		Root<EntityTestUser> root = cq.from(EntityTestUser.class);

		PredicateFilterRulesAccessor<EntityTestUser, FilterTestUser> accessor = new PredicateFilterRulesAccessor<>(
				filter, EntityTestUser.class, root, cq, cb);

		PredicateBuilder<EntityTestUser> builder = accessor.access();

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
	
	@Test(expected = PairValuesNotFoundException.class)
	public void whenCombinedFieldIsNotPaired() {
		Date start = Date.from(Instant.parse("2010-07-03T00:00:00.00Z"));

		FilterTestUser filter = new FilterTestUser();
		filter.setSinceStart(start);

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EntityTestUser> cq = cb.createQuery(EntityTestUser.class);
		Root<EntityTestUser> root = cq.from(EntityTestUser.class);

		PredicateFilterRulesAccessor<EntityTestUser, FilterTestUser> accessor = new PredicateFilterRulesAccessor<>(
				filter, EntityTestUser.class, root, cq, cb);

		accessor.access();
	}
}
