package com.moger.demo.entities;

import com.moger.demo.dataconstants.BookGenre;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private long id;

	@Column(nullable = false)
	private String title;

	private String image_url;

	@Column(name = "publication_year")
	private int publicationYear;

	@OneToMany(targetEntity = Publisher.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinTable(
			name = "book_publishers", 											// join table
			joinColumns = @JoinColumn(name = "book_id"), 						// Column referencing Book
			inverseJoinColumns = @JoinColumn(name = "publisher_id") 			// Column referencing Publisher
	)
	private List<Publisher> publishers= new ArrayList<>();

	@OneToMany(targetEntity = Author.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinTable(
			name = "book_authors", 												// join table
			joinColumns = @JoinColumn(name = "book_id"), 						// Column referencing Book
			inverseJoinColumns = @JoinColumn(name = "author_id") 				//  Column referencing Author
	)
	private List<Author> authors = new ArrayList<>();

	//@Enumerated(value = EnumType.STRING)
	@Column(name = "book_genre_id")
	private BookGenre genre;

	//@Pattern(regexp = "^\\d{1,2}(\\.\\d{1})?$")
	@Column(name = "amazon_rating")
	private double amazonRating;


    //Getters and Setters
	public List<Author> getAuthors() {

		if (authors == null) {
			authors = new ArrayList<Author>();
		}
		return authors;
	}

	public List<Publisher> getPublishers() {

		if (publishers == null) {
			publishers = new ArrayList<Publisher>();
		}
		return publishers;
	}

	public void setAuthors(List<Author> theAuthors) {
		this.authors.clear();
		theAuthors.stream().forEach(obj -> obj.setId(0));
		if (theAuthors != null) {
			this.authors.addAll(theAuthors);
		}
	}

	public void setPublishers(List<Publisher> thePublishers) {
		this.publishers.clear();
		thePublishers.stream().forEach(obj -> obj.setId(0));
		if (thePublishers != null) {
			this.publishers.addAll(thePublishers);
		}
	}
}

