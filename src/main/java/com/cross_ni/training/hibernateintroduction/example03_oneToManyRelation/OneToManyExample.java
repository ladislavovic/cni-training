package com.cross_ni.training.hibernateintroduction.example03_oneToManyRelation;

import com.cross_ni.training.hibernateintroduction.example01_columnMapping.JPAContexConfiguration;
import com.cross_ni.training.helper.JPAHelper;
import com.cross_ni.training.hibernateintroduction.model.Book;
import com.cross_ni.training.hibernateintroduction.model.Publisher;
import com.cross_ni.training.helper.Examples;
import org.hibernate.LazyInitializationException;
import org.junit.Test;

import static com.cross_ni.training.helper.SpringHelper.runInSpring;

public class OneToManyExample extends Examples {

	@Test
	public void persisting() {
		runInSpring(JPAContexConfiguration.class, ctx -> {
			JPAHelper jpaHelper = new JPAHelper(ctx);

			jpaHelper.doInTransactionAndFreshEM(entityManager -> {
				Publisher publisher = new Publisher("pub1");
				entityManager.persist(publisher);
				
				Book book = new Book("book1");
				book.setPublisher(publisher);
				entityManager.persist(book);
				
				return null;
			});
		});
	}
	
	@Test
	public void lazyInitializationException() {
		runInSpring(JPAContexConfiguration.class, ctx -> {
			JPAHelper jpaHelper = new JPAHelper(ctx);

			Long bookId = jpaHelper.doInTransactionAndFreshEM(entityManager -> {
				Publisher publisher = new Publisher("pub1");
				entityManager.persist(publisher);
				
				Book book = new Book("book1");
				book.setPublisher(publisher);
				entityManager.persist(book);
				
				return book.getId();
			});
			
			Book book = jpaHelper.doInTransactionAndFreshEM(entityManager -> {
				return entityManager.find(Book.class, bookId);
			});
			
			// it is OK, it just returns not initialized proxy object
			book.getPublisher();
			
			// here is the problem, you want to load data from the proxy which does not have them
			assertThrows(LazyInitializationException.class, () -> book.getPublisher().getName());
			
			// it is ok because id is in the proxy
			book.getPublisher().getId();
			
		});
	}
	
}
