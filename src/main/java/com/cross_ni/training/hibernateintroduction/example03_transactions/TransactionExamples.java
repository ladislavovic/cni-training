package com.cross_ni.training.hibernateintroduction.example03_transactions;

import com.cross_ni.training.hibernateintroduction.common.DBService;
import com.cross_ni.training.hibernateintroduction.common.JPAContexConfiguration;
import org.hibernate.internal.SessionImpl;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.UUID;

import static com.cross_ni.training.helper.SpringHelper.runInSpring;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TransactionExamples {

	@Test
	public void newTransactionHasAlsoNewEntityManager() {
		runInSpring(JPAContexConfiguration.class, ctx -> {
			DBService dbService = ctx.getBean(DBService.class);

			dbService.tx(entityManager1 -> {
				UUID id1 = getSessionIdentifier(entityManager1);
				
				dbService.txRequiresNew(entityManager2 -> {
					UUID id2 = getSessionIdentifier(entityManager2);
					assertNotEquals(id1, id2);
					return null;
				});
				
				dbService.tx(entityManager2 -> {
					UUID id2 = getSessionIdentifier(entityManager2);
					assertEquals(id1, id2);
					return null;
				});
				
			return null;
			});
		});
	}
	
	private UUID getSessionIdentifier(EntityManager em) {
		SessionImpl session = em.unwrap(SessionImpl.class);
		return session.getSessionIdentifier();
	}
	
}
