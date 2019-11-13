package com.cross_ni.training.hibernateintroduction.common;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.function.Function;

public class DBService {

	// Here Spring injects EntityManager. Actually it is a proxy which redirects to real EntityManager
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public <R> R tx(Function<EntityManager, R> job) {
		return job.apply(entityManager);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public <R> R txRequiresNew(Function<EntityManager, R> job) {
		return job.apply(entityManager);
	}
	
}
