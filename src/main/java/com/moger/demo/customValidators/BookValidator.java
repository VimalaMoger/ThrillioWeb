package com.moger.demo.customValidators;

import com.moger.demo.dataconstants.BookGenre;
import com.moger.demo.entities.Book;
import com.moger.demo.exception.MethodArgumentNotValidException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import java.util.Arrays;

@Component
public class BookValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);// || User.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors err) {
        Book book = (Book) obj;

        ValidationUtils.rejectIfEmpty(err, "genre", "genre.empty");

        BookGenre bookGenre = book.getGenre();
        BookGenre[] values = BookGenre.values();
        boolean bool = Arrays.stream(values).anyMatch(n -> n.equals(bookGenre));

        if(!bool){
            throw new MethodArgumentNotValidException("Book genre does not exist - Please try a new one");
        }

        if (book.getGenre().toString()== null) {
            err.rejectValue("genre", "It can not be empty");
        }
    }
}
