package com.cross_ni.training.helper;

import org.springframework.context.ApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.function.Function;

public class JPAHelper {

	private EntityManagerFactory emf;

	public JPAHelper(ApplicationContext ctx) {
		this.emf = ctx.getBean(EntityManagerFactory.class);
	}

	public EntityManager getEntityManager() {
		return emf.createEntityManager();
	}

	public <R> R doInTransactionAndFreshEM(Function<EntityManager, R> job) {
		EntityManager em = null;
		try {
			em = getEntityManager();
			return doInTransaction(em, job);
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public <R> R doInTransaction(EntityManager em, Function<EntityManager, R> job) {
		// TODO is this handling oK???
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		R result = job.apply(em);
		if (tx.getRollbackOnly()) {
			tx.rollback();
		} else {
			tx.commit();
		}
		return result;
	}

	public int countEntities(Class<?> clazz) {
		return doInTransactionAndFreshEM(entityManager -> {
			TypedQuery<Number> query = entityManager.createQuery("select count(*) from " + clazz.getName(), Number.class);
			Number count = query.getSingleResult();
			return count.intValue();
		});
	}
}

