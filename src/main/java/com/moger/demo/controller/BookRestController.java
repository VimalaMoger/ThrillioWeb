package com.moger.demo.controller;

import com.moger.demo.DTOs.BookPartialDTO;
import com.moger.demo.exception.MethodArgumentNotValidException;
import com.moger.demo.serviceImp.BookServiceImpl;
import com.moger.demo.entities.Book;
import com.moger.demo.exception.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

//REST Controller
@RestController
@RequestMapping("/api")
public class BookRestController {

    private final BookServiceImpl bookService;

    @Autowired
    public BookRestController(BookServiceImpl bookService) {

        this.bookService = bookService;
    }

    @PostMapping(value = "/books")
    public Book addBook(@RequestBody Book book) {

        if (book.getId() != 0) {
            throw new MethodArgumentNotValidException("All IDs updated automatically, id value in the request body must be zero!");
        }

        if (book.getTitle().equals("string"))
            throw new MethodArgumentNotValidException("Title must not be empty");

        if (book.getAmazonRating() < 1 || book.getAmazonRating() > 5) {
            throw new MethodArgumentNotValidException("Rating value must be between 1 and 5 inclusive");
        }

        return bookService.saveBook(book);
    }

    @GetMapping(value = "/books/{id}")
    public Book getBook(@PathVariable long id) {

        List<Book> books = bookService.getAllBooks().stream().filter(n -> n.getId() == id).collect(Collectors.toList());
        if(books.isEmpty())
            throw new BookNotFoundException(String.format("Book with id %d not found",id));

        return bookService.getBookById(id);
    }

    @GetMapping("/books")
    public List<Book> getAllBooks() {

        List<Book> books = bookService.getAllBooks();

        return books;
    }

    @PutMapping(value = "/books/{id}")
    public Book updateBook(@PathVariable long id, @RequestBody Book book) {

        List<Book> books = bookService.getAllBooks().stream().filter(n -> n.getId() == id).collect(Collectors.toList());
        if(books.isEmpty())
            throw new BookNotFoundException(String.format("Book with id %d not found",id));

        if (book.getId() != 0 ) {
            throw new MethodArgumentNotValidException("All IDs updated automatically, id value in the request body must be zero!");
        }

        if (book.getTitle().equals("string"))
            throw new MethodArgumentNotValidException("Title must not be empty");

        if (book.getAmazonRating() < 1 || book.getAmazonRating() > 5) {
            throw new MethodArgumentNotValidException("Rating value must be between 1 and 5 inclusive");
        }

        return bookService.updateBook(book, id);
    }

    @PatchMapping(value = "/books/{id}")
    public Book partialUpdateBook(@PathVariable long id, @RequestBody BookPartialDTO payload) {

        List<Book> books = bookService.getAllBooks().stream().filter(n -> n.getId() == id).collect(Collectors.toList());
        if(books.isEmpty())
            throw new BookNotFoundException(String.format("Book with id %d not found",id));

        if (payload.getTitle().equals("string"))
            throw new MethodArgumentNotValidException("Title must not be empty");

        if (payload.getPublicationYear() < 1700 || payload.getPublicationYear() > 2029)
            throw new MethodArgumentNotValidException("Publication year must be in the range of 1700-2029");

        return bookService.partialUpdateBook(payload, id);
    }

    @DeleteMapping(value = "/books/{id}")
    public String deleteBook(@PathVariable long id) {

        int result  = bookService.deleteBook(id);
        if(result > 0)
            return String.format(" Book with id %d is deleted", id);
        else
            throw new BookNotFoundException(String.format("Book with id %d is not found", id));
    }
}


