package com.nr.persistence.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import lombok.extern.slf4j.Slf4j;



@Slf4j
public class EntityManagerTestContext {

    private EntityManagerFactory factory = null;
    private EntityManager manager = null;
    private static final String PERSISTENCE_UNIT_TEST = "persistence-test";

    public EntityManager start() {
        this.factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_TEST);
        this.manager = this.factory.createEntityManager();
        this.manager.getTransaction().begin();
        return this.manager;
    }

    public void end() {
        try {
            this.manager.getTransaction().commit();
            this.manager.close();
            this.factory.close();
        } catch (final Exception e) {
            log.error("Error on close entity manager.", e);
        }
    }

    public EntityManager getEntityManager() {
        return this.manager;
    }

    public void commitAndOpenTransaction() {
        this.manager.getTransaction().commit();
        this.manager.getTransaction().begin();
    }
}
