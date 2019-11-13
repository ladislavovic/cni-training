package com.cross_ni.training.hibernateintroduction.example02_lazyFetching;

import com.cross_ni.training.helper.Examples;
import com.cross_ni.training.hibernateintroduction.common.DBService;
import com.cross_ni.training.hibernateintroduction.common.JPAContexConfiguration;
import com.cross_ni.training.hibernateintroduction.model.Book;
import com.cross_ni.training.hibernateintroduction.model.Publisher;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.junit.Test;

import javax.persistence.TypedQuery;

import static com.cross_ni.training.helper.SpringHelper.runInSpring;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("Duplicates")
public class LazyFetchingExamples extends Examples {

	@Test
	public void eagerFetching() {
		runInSpring(JPAContexConfiguration.class, ctx -> {

			DBService dbService = ctx.getBean(DBService.class);

			// create entities
			Long[] ids = dbService.tx(entityManager -> {
				Publisher albatros = new Publisher("Albatros");
				entityManager.persist(albatros);

				Book book1 = new Book("book1");
				book1.setPublisher(albatros);
				entityManager.persist(book1);

				return new Long[]{ albatros.getId(), book1.getId() };
			});
			Long publisherId = ids[0];
			Long bookId = ids[1];
			
			//
			// load as lazy
			//
			Book book = dbService.tx(entityManager -> entityManager.find(Book.class, bookId));
			
			// it is OK, it just returns not initialized proxy object
			book.getPublisher();

			// here is the problem, you want to load data from the proxy which does not have them
			assertThrows(LazyInitializationException.class, () -> book.getPublisher().getName());

			// it is ok because id is in the proxy
			book.getPublisher().getId();
			
			//
			// init by touch
			//
			Book book2 = dbService.tx(entityManager -> {
				Book b = entityManager.find(Book.class, bookId);
				b.getPublisher().getName(); // here it initialize the proxy, it means load other data from DB
				return b;
			});
			assertTrue(Hibernate.isInitialized(book2.getPublisher()));
			assertEquals("Albatros", book2.getPublisher().getName());

			//
			// init by join fetch
			//
			Book book3 = dbService.tx(entityManager -> {
				String queryStr = String.join(
						"\n",
						"select b",
						"from",
						"  Book b",
						"  join fetch b.publisher",
						"where b.id = :book_id");
				TypedQuery<Book> query = entityManager.createQuery(queryStr, Book.class);
				query.setParameter("book_id", bookId);
				return query.getSingleResult();
			});
			assertTrue(Hibernate.isInitialized(book3.getPublisher()));
			assertEquals("Albatros", book3.getPublisher().getName());

			//
			// init by Hibernate util method
			//
			Book book4 = dbService.tx(entityManager -> {
				Book b = entityManager.find(Book.class, bookId);
				Hibernate.initialize(b.getPublisher());
				return b;
			});
			assertTrue(Hibernate.isInitialized(book4.getPublisher()));
			assertEquals("Albatros", book4.getPublisher().getName());

			//
			// init by FetchProfile
			//
			Book book5 = dbService.tx(entityManager -> {
				Session sess = entityManager.unwrap(Session.class); // fetch profile is not part of JPA API
				sess.enableFetchProfile(Book.FETCH_PUBLISHER);
				Book b = entityManager.find(Book.class, bookId);
				return b;
			});
			assertTrue(Hibernate.isInitialized(book5.getPublisher()));
			assertEquals("Albatros", book5.getPublisher().getName());
			
		});
	}
	
}
