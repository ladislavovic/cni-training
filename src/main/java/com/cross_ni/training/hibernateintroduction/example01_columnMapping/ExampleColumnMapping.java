package com.cross_ni.training.hibernateintroduction.example01_columnMapping;

import com.cross_ni.training.helper.JPAHelper;
import com.cross_ni.training.hibernateintroduction.model.Publisher;
import com.cross_ni.training.helper.Examples;
import org.junit.Assert;
import org.junit.Test;

import static com.cross_ni.training.helper.SpringHelper.runInSpring;

public class ExampleColumnMapping extends Examples {
	
	@Test
	public void example() {
		runInSpring(JPAContexConfiguration.class, ctx -> {
			JPAHelper jpaHelper = new JPAHelper(ctx);
			
			Assert.assertEquals(0, jpaHelper.countEntities(Publisher.class));
			
			jpaHelper.doInTransactionAndFreshEM(entityManager -> {
				Publisher publisher = new Publisher();
				publisher.setName("Albatros");
				entityManager.persist(publisher);
				return publisher;
			});

			Assert.assertEquals(1, jpaHelper.countEntities(Publisher.class));
		});
	}
	
}
