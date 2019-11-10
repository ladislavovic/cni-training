package com.cross_ni.training.hibernateintroduction.model;

import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfiles;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "book")
@FetchProfiles({
		@FetchProfile(name = Book.FETCH_PUBLISHER, fetchOverrides = {
				@FetchProfile.FetchOverride(entity = Book.class, association = "publisher", mode = FetchMode.JOIN)
		})
})
public class Book {
	
	public static final String FETCH_PUBLISHER = "NODE_FETCH_PUBLISHER";

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String name;

	// Owning side of the relation
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Publisher publisher;

	public Book() {
	}

	public Book(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
}
