package com.moger.demo.util;

import com.moger.demo.config.AppConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.stereotype.Component;

@Component
public class QueryData {

	private AppConfig config;
	String encodedPassword;

	public QueryData(AppConfig appConfig) {
		this.config = appConfig;
		encodedPassword = config.passwordEncoder().encode(config.userPassword);
	 }

    public void loadData() {
		String sqlUser = "INSERT INTO user(email, password, first_name, last_name, gender_id, user_type_id, created_date) values('user0@semanticsquare.com', :password, 'John', 'M', 0, 0, NOW()), ('user1@semanticsquare.com', :password,'Sam', 'M', 0, 0, NOW()),('user2@semanticsquare.com', :password, 'Anita', 'M', 1, 1, NOW()),('user3@semanticsquare.com', :password, 'Sara', 'M', 1, 1, NOW()),('user4@semanticsquare.com', :password, 'Dheeru', 'M', 0,2, NOW())";
		String sqlPublisher = "INSERT INTO publisher(name) values('Wilder'),('Dover Publications'),('Touchstone'),('O''Reilly Media'),('Prentice Hall')";
		String sqlAuthor = "INSERT INTO author(name) values('Henry David Thoreau'),('Ralph Waldo Emerson'),('Lillian Eichler Watson'),('Eric Freeman'),('Bert Bates'),('Kathy Sierra'), ('Elisabeth Robson'),('Joshua Bloch')";
		String sqlBook ="INSERT INTO book(title, publication_year, book_genre_id, amazon_rating, image_url) values('Walden',1854, 6, 4.3, 'https://images.gr-assets.com/books/1465675526l/16902.jpg'),('Self-Reliance and Other Essays', 1993, 6, 4.5, 'https://images.gr-assets.com/books/1520778510l/123845.jpg'),('Light From Many Lamps', 1988, 6, 5.0, 'https://images.gr-assets.com/books/1347739312l/1270698.jpg'),('Head First Design Patterns', 2004, 10, 4.5, 'https://images.gr-assets.com/books/1408309444l/58128.jpg'),('Effective Java Programming Language Guide', 2007, 10, 4.9, 'https://images.gr-assets.com/books/1433511045l/105099.jpg')";
		String sqlBookAuthor = "INSERT INTO book_authors(book_id, author_id) values(1, 1), (2, 2), (3, 3), (4, 4), (4, 5), (4, 6), (4,7), (5, 8)";
		String sqlBookPublisher = "INSERT INTO book_publishers(book_id, publisher_id) values(1, 1), (2, 2), (3, 3), (4, 4), (5, 5)";
		String sqlBookUser = "INSERT INTO book_user(book_id, user_id) values(4, 5),(4, 1),(3, 1),(1, 2),(2, 3), (5, 4)";
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-tutorials");
        EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.createNativeQuery(sqlUser).setParameter("password", encodedPassword).executeUpdate();
		entityManager.createNativeQuery(sqlPublisher).executeUpdate();
		entityManager.createNativeQuery(sqlAuthor).executeUpdate();
		entityManager.createNativeQuery(sqlBook).executeUpdate();
		entityManager.createNativeQuery(sqlBookAuthor).executeUpdate();
		entityManager.createNativeQuery(sqlBookPublisher).executeUpdate();
		entityManager.createNativeQuery(sqlBookUser).executeUpdate();
		entityManager.getTransaction().commit();
        }
}
