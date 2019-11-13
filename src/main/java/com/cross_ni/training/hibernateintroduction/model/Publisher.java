package com.cross_ni.training.hibernateintroduction.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "publisher")
public class Publisher {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false, length = 128)
	private String name;
	
	@Column(nullable = true, unique = true)
	private String identificationNum;

	@OneToMany(mappedBy = "publisher", fetch = FetchType.LAZY)
	private Set<Book> books = new HashSet<>();

	public Publisher() {
	}

	public Publisher(String name) {
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

	public String getIdentificationNum() {
		return identificationNum;
	}

	public void setIdentificationNum(String identificationNum) {
		this.identificationNum = identificationNum;
	}

	public Set<Book> getBooks() {
		return books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}
}
