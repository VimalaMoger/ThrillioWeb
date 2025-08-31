package com.moger.demo.service;

import com.moger.demo.entities.Book;
import com.moger.demo.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> authenticate(String email, String password);

    void saveUser(User user);

    User getUserByEmail(String email);

    List<Book> getAllBooksAddedByUser(Long id);
}
