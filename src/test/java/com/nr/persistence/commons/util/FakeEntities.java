package com.nr.persistence.commons.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.persistence.EntityManager;

import com.nr.persistence.test.EntityTestDepartment;
import com.nr.persistence.test.EntityTestLocation;
import com.nr.persistence.test.EntityTestUser;

public class FakeEntities {

	public static void createFakeEntities(EntityManager em) {
		EntityTestLocation locJoi = new EntityTestLocation(1, "Joinville");
		EntityTestLocation locCtba = new EntityTestLocation(2, "Curitiba");
		EntityTestDepartment dep = new EntityTestDepartment(1, "Dep 1", locJoi);
		EntityTestDepartment dep2 = new EntityTestDepartment(2, "Dep 2", locCtba);

		for (int i = 0; i < 10; i++) {
			EntityTestUser user = new EntityTestUser();
			user.setId(i);
			user.setName("Test");
			user.setSince(Date.from(Instant.parse("2017-07-03T10:15:30.00Z").minus(i * 30, ChronoUnit.DAYS)));
			user.setEnabled(new Boolean(i % 2 == 0));
			user.setNumberOfChildren(i);
			user.setScore(i + 0.1);
			user.setDepartment(i % 4 == 0 ? dep : dep2);

			em.persist(user);
		}

		for (int i = 10; i < 30; i++) {
			EntityTestUser user = new EntityTestUser();
			user.setId(i);
			user.setName("User " + (i - 9));
			user.setSince(Date.from(Instant.parse("2012-12-03T10:15:30.00Z").minus(i * 30, ChronoUnit.DAYS)));
			user.setEnabled(new Boolean(i % 2 != 0));
			user.setNumberOfChildren(i % 4 == 0 ? 1 : 2);
			user.setScore(i - 9.9);
			user.setDepartment(dep2);

			em.persist(user);
		}
	}

}
