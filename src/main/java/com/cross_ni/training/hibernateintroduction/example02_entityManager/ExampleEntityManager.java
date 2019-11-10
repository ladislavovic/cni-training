package com.cross_ni.training.hibernateintroduction.example02_entityManager;

import com.cross_ni.training.hibernateintroduction.example01_columnMapping.JPAContexConfiguration;
import com.cross_ni.training.helper.JPAHelper;
import com.cross_ni.training.hibernateintroduction.model.Publisher;
import com.cross_ni.training.helper.Examples;
import org.junit.Assert;
import org.junit.Test;

import static com.cross_ni.training.helper.SpringHelper.runInSpring;

public class ExampleEntityManager extends Examples {

	@Test
	public void persist() {
		runInSpring(JPAContexConfiguration.class, ctx -> {
			JPAHelper jpaHelper = new JPAHelper(ctx);

			Assert.assertEquals(0, jpaHelper.countEntities(Publisher.class));

			jpaHelper.doInTransactionAndFreshEM(entityManager -> {
				Publisher publisher = new Publisher("Albatros");
				entityManager.persist(publisher);
				return null;
			});

			Assert.assertEquals(1, jpaHelper.countEntities(Publisher.class));
		});
	}
	
	@Test
	public void merge() {
		runInSpring(JPAContexConfiguration.class, ctx -> {
			JPAHelper jpaHelper = new JPAHelper(ctx);

			Publisher albatros = jpaHelper.doInTransactionAndFreshEM(entityManager -> {
				Publisher publisher = new Publisher("Albatros");
				entityManager.persist(publisher);
				return publisher;
			});

			albatros.setName("Cpress");
			
			jpaHelper.doInTransactionAndFreshEM(entityManager -> {
				entityManager.merge(albatros);
				return null;
			});

			jpaHelper.doInTransactionAndFreshEM(entityManager -> {
				Publisher publisher = entityManager.find(Publisher.class, albatros.getId());
				Assert.assertEquals("Cpress", publisher.getName());
				return null;
			});
			
		});
	}
	
	@Test
	public void implicitMerge() {
		runInSpring(JPAContexConfiguration.class, ctx -> {
			JPAHelper jpaHelper = new JPAHelper(ctx);

			Publisher albatros = jpaHelper.doInTransactionAndFreshEM(entityManager -> {
				Publisher publisher = new Publisher("Albatros");
				entityManager.persist(publisher);
				publisher.setName("Cpress");
				return publisher;
			});

			jpaHelper.doInTransactionAndFreshEM(entityManager -> {
				Publisher publisher = entityManager.find(Publisher.class, albatros.getId());
				Assert.assertEquals("Cpress", publisher.getName());
				return null;
			});
			
		});
	}
	
}
