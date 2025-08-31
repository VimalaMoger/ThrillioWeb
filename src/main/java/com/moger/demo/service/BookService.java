package com.moger.demo.service;

import com.moger.demo.entities.Book;
import com.moger.demo.entities.User;
import java.util.List;
import java.util.Map;

public interface BookService {

    Book saveBook(Book emp);

    List<Book> getAllBooks();

    Book getBookById(Long id);

    Book updateBook(Book book, Long id);

    Book partialUpdateBook(Map<String, Object> payload, Long id);

    int deleteBook(Long id);

    void saveBookUser(Book book, User user);

    Book saveBooks(Long id);



}
