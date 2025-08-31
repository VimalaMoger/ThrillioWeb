package com.moger.demo.customValidators;

import com.moger.demo.entities.Book;
import com.moger.demo.entities.User;
import com.moger.demo.exception.MethodArgumentNotValidException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz)|| Book.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors err) {

        User user = (User) obj;

        ValidationUtils.rejectIfEmpty(err, "email", "email.empty");

        String isValidEmail = "^[A-Za-z0-9]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        boolean validEmail = Pattern.compile(isValidEmail).matcher(user.getEmail()).matches();

        if(!validEmail) {
            throw new MethodArgumentNotValidException("Invalid Email address. Please enter a valid format");
        }

        if (user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            err.rejectValue("email", "It can not be empty");
        }
    }
}
