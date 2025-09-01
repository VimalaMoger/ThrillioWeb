package com.moger.demo.serviceImp;

import com.moger.demo.DTOs.BookDTO;
import com.moger.demo.entities.Author;
import com.moger.demo.entities.Book;
import com.moger.demo.entities.Publisher;
import java.util.ArrayList;
import java.util.List;

public class BookDTOService {

    public List<BookDTO>  getBooks(List<Book> books){
        List<BookDTO> bookDTOS = new ArrayList<>();

        for(Book book : books){

            BookDTO bookDTO = new BookDTO();
            bookDTO.setId(book.getId());
            bookDTO.setTitle(book.getTitle());
            bookDTO.setImage_url(book.getImage_url());
            bookDTO.setPublicationYear(book.getPublicationYear());
            bookDTO.setGenre(book.getGenre());
            bookDTO.setAmazonRating(book.getAmazonRating());

            List<String> aNames = book.getAuthors().stream().filter((obj) -> obj.getName() != null && !obj.getName().isEmpty()).map(Author::getName).toList();
            bookDTO.setAuthorNames(aNames);

            List<String> pNames = book.getPublishers().stream().filter((obj) -> obj.getName() != null && !obj.getName().isEmpty()).map(Publisher::getName).toList();
            bookDTO.setPublisherNames(pNames);

            bookDTOS.add(bookDTO);

        }
        return bookDTOS;
    }

    public BookDTO  getBook(Book book){

        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setImage_url(book.getImage_url());
        bookDTO.setPublicationYear(book.getPublicationYear());
        bookDTO.setGenre(book.getGenre());
        bookDTO.setAmazonRating(book.getAmazonRating());

        List<String> aNames = book.getAuthors().stream().filter((obj) -> obj.getName() != null && !obj.getName().isEmpty()).map(Author::getName).toList();
        bookDTO.setAuthorNames(aNames);

        List<String> pNames = book.getPublishers().stream().filter((obj) -> obj.getName() != null && !obj.getName().isEmpty()).map(Publisher::getName).toList();
        bookDTO.setPublisherNames(pNames);

        return bookDTO;
    }
}
