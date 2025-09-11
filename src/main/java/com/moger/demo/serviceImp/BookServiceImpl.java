package com.moger.demo.serviceImp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moger.demo.DTOs.BookPartialDTO;
import com.moger.demo.config.AppConfig;
import com.moger.demo.entities.*;
import com.moger.demo.exception.BookNotFoundException;
import com.moger.demo.exception.DataNotFoundException;
import com.moger.demo.exception.MethodArgumentNotValidException;
import com.moger.demo.service.BookService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.query.MutationQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;


@Service
public class BookServiceImpl implements BookService {

    @Autowired
    AppConfig appConfig;

    private EntityManager em;
    private ObjectMapper objectMapper;

    @Autowired
    public BookServiceImpl(EntityManager em, ObjectMapper objectMapper, AppConfig appConfig) {

        this.em = em;
        this.objectMapper = objectMapper;
        this.appConfig = appConfig;
    }

    @Transactional
    @Override
    public Book saveBook(Book book) {

        if(book == null){
            throw new MethodArgumentNotValidException(String.format("Enter valid book details - book is %s -", null));
        }
        em.persist(book);
        em.flush();

        return book;
    }

    @Transactional
    @Override
    public List<Book> getAllBooks() throws DataNotFoundException {

        TypedQuery<Book> query = em.createQuery("FROM Book", Book.class);

        List<Book> books = query.getResultList();
        if (books.isEmpty())
            throw new DataNotFoundException(String.format("The books don't exist. Please add a new one %s", books));

        return books;
    }

    @Transactional
    @Override
    public Book getBookById(Long id) throws BookNotFoundException, MethodArgumentNotValidException {

        Session session = em.unwrap(Session.class);
        TypedQuery<Book> query = session.createQuery("FROM Book  WHERE id = ?1 ", Book.class);
        query.setParameter(1, id);
        Book book = query.getSingleResult();

        if (book == null) {
            throw new BookNotFoundException(String.format("Book with id %d not found", id));
        }
        return book;
    }

    @Transactional
    @Override
    public Book updateBook(Book book, Long eId) throws BookNotFoundException, MethodArgumentNotValidException {

        Book theBook = getBookById(eId);
        if (theBook == null) {
            throw new RuntimeException(String.format("Book id %d not found -", eId));
        }

        //child entity - author
        theBook.getAuthors().clear();
        List<Author> bookAuthors = book.getAuthors();
        if (bookAuthors != null) {
            bookAuthors.stream().forEach(obj -> obj.setId(0));
            theBook.setAuthors(bookAuthors);
        }

        //child entity - publisher
        theBook.getPublishers().clear();
        List<Publisher> bookPublishers = book.getPublishers();
        if (bookPublishers != null) {
            bookPublishers.stream().forEach(obj -> obj.setId(0));
            theBook.setPublishers(book.getPublishers());
        }

        theBook.setGenre(book.getGenre());
        theBook.setAmazonRating(book.getAmazonRating());
        theBook.setPublicationYear(book.getPublicationYear());
        theBook.setTitle(book.getTitle());
        theBook.setImage_url(book.getImage_url());

        em.persist(theBook);
        return theBook;
    }

    @Transactional
    @Override
    public Book partialUpdateBook(BookPartialDTO payload, Long id) {

        Book theBook = getBookById(id);
        if (theBook == null) {
            throw new RuntimeException(String.format("Book id %d not found -", id));
        }

        Book patchBook = apply(payload, theBook);

        Book mergedBook = em.merge(patchBook);
        em.flush();

        return mergedBook;
    }

    @Transactional
    @Override
    public int deleteBook(Long id) {

        List<Book> books = getAllBooks();
        if (books.size() == 1){
            throw new MethodArgumentNotValidException("Sorry, this is the last entry in book-library. Add a new entry before performing this action");
        }
        Session session = em.unwrap(Session.class);
        session.createNativeMutationQuery("DELETE from book_user WHERE book_id =:id")
                .setParameter("id", id).executeUpdate();
        session.createNativeMutationQuery("DELETE from book_publishers WHERE book_id =:id")
                .setParameter("id", id).executeUpdate();
        session.createNativeMutationQuery("DELETE from book_authors WHERE book_id =:id")
                .setParameter("id", id).executeUpdate();
        MutationQuery query = session.createNativeMutationQuery("DELETE from book WHERE id =:id");
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    @Transactional
    @Override
    public void saveBookUser(Book book, User user) throws DataNotFoundException {

        if(book == null || user == null)
            throw new DataNotFoundException(String.format("Book details are not entered -%s", book));

        BookUserId bookUserId = new BookUserId();
        bookUserId.setBookId(book.getId());
        bookUserId.setUserId(user.getId());
        BookUser bookUser = new BookUser();
        bookUser.setBook(book);
        bookUser.setUser(user);
        bookUser.setId(bookUserId);
        em.persist(bookUser);

        em.flush();
    }

    @Transactional
    @Override
    public Book saveBooks(Long id) {

        Book bk = getBookById(id);
        if(bk != null)
          return bk;
        return null;
    }
    // Partial data to book(Title, image url, publication year
    private Book apply(BookPartialDTO payload, Book book) {

        //convert book object to a JSON object node
        ObjectNode bookNode = objectMapper.convertValue(book, ObjectNode.class);

        //convert payload to JSON object node
        ObjectNode patchNode = objectMapper.convertValue(payload, ObjectNode.class);

        //merge the patch update into bookNode
        bookNode.setAll(patchNode);

        return objectMapper.convertValue(bookNode, Book.class);
    }

    public List<Author> getAuthors() {

        TypedQuery<Author> query = em.createQuery("FROM Author", Author.class);

        List<Author> authors = query.getResultList();
        return authors;
    }
}	

	

