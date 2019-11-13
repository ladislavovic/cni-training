package com.cross_ni.training.hibernateintroduction.example01_entityManager;

import com.cross_ni.training.helper.Examples;
import com.cross_ni.training.hibernateintroduction.common.DBService;
import com.cross_ni.training.hibernateintroduction.common.JPAContexConfiguration;
import com.cross_ni.training.hibernateintroduction.model.Publisher;
import org.hibernate.Session;
import org.junit.Test;

import javax.persistence.TypedQuery;
import java.util.List;

import static com.cross_ni.training.helper.SpringHelper.runInSpring;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EntityManagerExamples extends Examples {
	
	@Test
	public void commonOperationsOnEntityManager() {
		runInSpring(JPAContexConfiguration.class, ctx -> {
			DBService dbService = ctx.getBean(DBService.class);

			// create, read
			Long publisherId = dbService.tx(entityManager -> {
				Publisher albatros = new Publisher("Albatros");
				entityManager.persist(albatros);
				return albatros.getId();
			});
			assertEquals("Albatros", dbService.tx(em -> em.find(Publisher.class, publisherId).getName()));
			
			// update managed entity
			Publisher detached = dbService.tx(entityManager -> {
				Publisher publisher = entityManager.find(Publisher.class, publisherId);
				publisher.setName("CPress");
				return publisher;
			});
			assertEquals("CPress", dbService.tx(em -> em.find(Publisher.class, publisherId).getName()));

			// update detached entity
			dbService.tx(entityManager -> {
				detached.setName("OReilly");
				entityManager.merge(detached);
				return null;
			});
			assertEquals("OReilly", dbService.tx(em -> em.find(Publisher.class, publisherId).getName()));
			
			// query
			dbService.tx(entityManager -> {
				TypedQuery<Publisher> query = entityManager.createQuery(
						"select p from Publisher p where p.name like 'ORei%'",
						Publisher.class);
				List<Publisher> resultList = query.getResultList();
				assertEquals(1, resultList.size());
				return null;
			});
			
			// delete
			dbService.tx(entityManager -> {
				Publisher pub = entityManager.getReference(Publisher.class, publisherId);
				entityManager.remove(pub);
				return null;
			});
			assertNull(dbService.tx(em -> em.find(Publisher.class, publisherId)));
			
		});
	}
	
	@Test
	public void entityManagerState() {
		runInSpring(JPAContexConfiguration.class, ctx -> {
			DBService dbService = ctx.getBean(DBService.class);

			dbService.tx(entityManager -> {
				
				// it is empty at the beginning
				assertEquals(0, entityManager.unwrap(Session.class).getStatistics().getEntityCount());
				
				// if you persist an entity, it is added to the entity manager
				Publisher publisher = new Publisher("Albatros");
				entityManager.persist(publisher);
				assertEquals(1, entityManager.unwrap(Session.class).getStatistics().getEntityCount());
				entityManager.flush(); // must be stored to DB before detach() method call
				Long publisherId = publisher.getId();
				
				// you can remove entity from the entity manager
				entityManager.detach(publisher);
				assertEquals(0, entityManager.unwrap(Session.class).getStatistics().getEntityCount());
				
				// when you load it it is also added to the entity manager
				publisher = entityManager.find(Publisher.class, publisherId);
				assertEquals(1, entityManager.unwrap(Session.class).getStatistics().getEntityCount());
				
				// when you remove the entity it will be removed from the DB but it remains in the entity manager
				entityManager.remove(publisher);
				assertEquals(1, entityManager.unwrap(Session.class).getStatistics().getEntityCount());
				
				return null;
			});
		});
	}
	
	
	
	
	
}
