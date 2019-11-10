package com.cross_ni.training.hibernateintroduction.example04_fetchProfile;

import com.cross_ni.training.hibernateintroduction.example01_columnMapping.JPAContexConfiguration;
import com.cross_ni.training.helper.JPAHelper;
import com.cross_ni.training.hibernateintroduction.model.Book;
import com.cross_ni.training.hibernateintroduction.model.Publisher;
import com.cross_ni.training.helper.Examples;
import org.hibernate.Session;
import org.junit.Test;

import static com.cross_ni.training.helper.SpringHelper.runInSpring;

@SuppressWarnings("Duplicates")
public class FetchProfileExample extends Examples {

	@Test
	public void loadWithFetchProfile() {
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
				Session sess = entityManager.unwrap(Session.class);
				sess.enableFetchProfile(Book.FETCH_PUBLISHER);
				return entityManager.find(Book.class, bookId);
			});
			
			book.getPublisher().getName();
			
		});
	}
	
}
