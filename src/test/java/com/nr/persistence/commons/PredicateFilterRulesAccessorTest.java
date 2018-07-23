package com.nr.persistence.commons;

import java.time.Instant;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.Before;
import org.junit.Test;

import com.nr.persistence.commons.FilterField;
import com.nr.persistence.commons.FilterOperation;
import com.nr.persistence.commons.PredicateFilterRulesAccessor;
import com.nr.persistence.commons.util.FakeEntities;
import com.nr.persistence.query.dynamic.exception.MandatoryAnnotationProperty;
import com.nr.persistence.test.EntityManagerTestContext;
import com.nr.persistence.test.EntityTestUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class PredicateFilterRulesAccessorTest {
	private EntityManagerTestContext entityManagerContext;

	private EntityManager em;

	@Before
	public void setup() {
		entityManagerContext = new EntityManagerTestContext();

		em = entityManagerContext.start();
		FakeEntities.createFakeEntities(em);
		entityManagerContext.commitAndOpenTransaction();
	}

	@Test(expected = MandatoryAnnotationProperty.class)
	public void noEntityNameOnBetweenTest() {
		Date start = Date.from(Instant.parse("2011-07-03T00:00:00.00Z"));
		Date end = Date.from(Instant.parse("2017-07-31T00:00:00.00Z"));
		WrongFilter filter = new WrongFilter(start, end);

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EntityTestUser> query = cb.createQuery(EntityTestUser.class);
		Root<EntityTestUser> root = query.from(EntityTestUser.class);

		PredicateFilterRulesAccessor<EntityTestUser, WrongFilter> accessor = new PredicateFilterRulesAccessor<>(
				filter, EntityTestUser.class, root, (CriteriaQuery<EntityTestUser>) query, cb);

		accessor.access();
	}

	@Getter
	@Setter
	@AllArgsConstructor
	public class WrongFilter {

		@FilterField(operation = FilterOperation.BETWEEN_START)
		private Date sinceStart;

		@FilterField(operation = FilterOperation.BETWEEN_END)
		private Date sinceEnd;
	}

}
