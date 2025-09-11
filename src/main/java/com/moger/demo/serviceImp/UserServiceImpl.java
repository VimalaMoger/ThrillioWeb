package com.moger.demo.serviceImp;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.moger.demo.config.AppConfig;
import com.moger.demo.dataconstants.Gender;
import com.moger.demo.entities.Book;
import com.moger.demo.entities.User;
import com.moger.demo.exception.DataAlreadyExistsException;
import com.moger.demo.exception.DataNotFoundException;
import com.moger.demo.exception.MethodArgumentNotValidException;
import com.moger.demo.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    AppConfig appConfig;

    private EntityManager em;
    private ObjectMapper objectMapper;

    @Autowired
    public UserServiceImpl(EntityManager em, ObjectMapper objectMapper, AppConfig appConfig) {

        this.em = em;
        this.objectMapper = objectMapper;
        this.appConfig = appConfig;
    }

    @Transactional
    @Override
    public Optional<User> authenticate(String email, String password) throws MethodArgumentNotValidException {

        try {
            Session session = em.unwrap(Session.class);
            Query query = session.createQuery("From User where email ='" + email + "' ", User.class);
            return Optional.ofNullable((User) query.getSingleResult());
        }catch (Exception ex) {
            throw new DataNotFoundException("User with email "+email+"  does not exist in our system");
        }
    }

    @Transactional
    @Override
    public void saveUser(User newUser) throws MethodArgumentNotValidException {

        //check if email already exists in the database before saving a new user details
        Query query = em.createNativeQuery("select * from user where email ='" + newUser.getEmail() + "' ", User.class);
        if(!query.getResultList().isEmpty()){
            throw new DataAlreadyExistsException("Email already exists.Please enter a valid email address");
        }
        //save user
        try {
            String encryptedPassword = appConfig.passwordEncoder().encode(newUser.getPassword());
            newUser.setPassword(encryptedPassword);
            newUser.setCreated_date(new Date());
            newUser.setUserType("user");
            newUser.setGender(Gender.UNKNOWN);

            Session session = em.unwrap(Session.class);
            session.persist(newUser);
        } catch (MethodArgumentNotValidException ex) {
            throw new MethodArgumentNotValidException("The data you entered is missing valid information");
        }

    }

    @Transactional
    @Override
    public User getUserByEmail(String email) throws DataNotFoundException {

        Session session = em.unwrap(Session.class);
        TypedQuery<User> query = session.createQuery("FROM User WHERE email =?1", User.class);
        query.setParameter(1, email);

        User user = query.getSingleResult();
        if (user == null) {
            throw new DataNotFoundException(String.format("User with %s not found", email));
        }
        return user;
    }

    @Transactional
    @Override
    public List<Book> getAllBooksAddedByUser(Long id) throws DataNotFoundException {

        Session session = em.unwrap(Session.class);
        NativeQuery<Book> query = session.createNativeQuery("select * from book b WHERE b.id in (select book_id from book_user where user_id=:userId)", Book.class);
        query.setParameter("userId", id);

        List<Book> books = query.getResultList();
        if (books.isEmpty())
            throw new DataNotFoundException(String.format("There are no books added by user with Id %d", id));

        return books;
    }

}
