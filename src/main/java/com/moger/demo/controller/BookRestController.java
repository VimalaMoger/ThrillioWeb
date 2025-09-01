package com.moger.demo.controller;

import com.moger.demo.serviceImp.BookServiceImpl;
import com.moger.demo.entities.Book;
import com.moger.demo.exception.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

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

        return bookService.saveBook(book);
    }

    @GetMapping(value = "/books/{id}")
    public Book getBook(@PathVariable long id) {

        if(id > bookService.getAllBooks().size())
            throw new BookNotFoundException(String.format("Book with %d not found",id));

        return bookService.getBookById(id);
    }

    @GetMapping("/books")
    public List<Book> getAllBooks() {

        List<Book> books = bookService.getAllBooks();

        return books;
    }

    @PutMapping(value = "/books/{id}")
    public Book updateBook(@PathVariable long id, @RequestBody Book book) {

        return bookService.updateBook(book, id);
    }

    @PatchMapping(value = "/books/{id}")
    public Book partialUpdateBook(@PathVariable long id, @RequestBody Map<String, Object> payload) {

        return bookService.partialUpdateBook(payload, id);
    }

    @DeleteMapping(value = "/books/{id}")
    public String deleteBook(@PathVariable long id) {

        int result  = bookService.deleteBook(id);
        if(result > 0)
            return String.format(" Book with %d is deleted", id);
        else
            throw new BookNotFoundException(String.format("Book with %d is not found", id));
    }
}


