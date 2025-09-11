package com.moger.demo.controller;

import com.moger.demo.config.AppConfig;
import com.moger.demo.entities.Book;
import com.moger.demo.entities.User;
import com.moger.demo.exception.MethodArgumentNotValidException;
import com.moger.demo.customValidators.UserValidator;
import com.moger.demo.service.UserService;
import com.moger.demo.serviceImp.BookDTOService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Validated
@Controller
public class UserController {

    @Autowired
    private AppConfig appConfig;

    String email;
    private UserService userService;

    // Temporary in-memory storage - bookmark books
    private final Map<Long, Book> bookmarks = new ConcurrentHashMap<>();

    public UserController(UserService service, AppConfig appConfig) {
        this.userService = service;
        this.appConfig = appConfig;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new UserValidator());
    }

    @GetMapping("/")
    public String login_User(Model model) {

        User user= new User();
        model.addAttribute("loginForm", user);

        return "index";
    }

    @PostMapping("/users")
    public String validate_User(@Valid @ModelAttribute(name="loginForm") User user, Model model, BindingResult result, HttpSession session) {

        if (result.hasErrors()){
            model.addAttribute("error", "Invalid email or password. Please try again");
            return "index";
        }

        String email = user.getEmail();
        String password = user.getPassword();
        Optional<User> optionalUser = userService.authenticate(email, password);

        //set session attribute
        session.setAttribute("name", email);

        if(optionalUser.isPresent()) {
            if (appConfig.passwordEncoder().matches(password,optionalUser.get().getPassword()))
                return "redirect:/books";
        }
        if(optionalUser.isEmpty()){
            throw new MethodArgumentNotValidException("User does not exist in our system");
        }
        model.addAttribute("error", "Incorrect credentials. Please try again");
        return "index";
    }

    @GetMapping("/users/new")
    public String register_User(Model model) {

        User user= new User();
        model.addAttribute("registrationForm", user);
        return "register";
    }

    @PostMapping("/users/new")
    public String save_User(@Valid @ModelAttribute(name="registrationForm") User user, Model model, BindingResult result) {

        if (result.hasErrors()) {
            model.addAttribute("error", "Invalid email or password");
            return "redirect:/users/new";
        }
        userService.saveUser(user);
        return "redirect:/users/new?success";
    }

    @GetMapping("/users/books")
    public String getBooks_AddedBy_User(Model model, HttpSession session) {

        String email = (String) session.getAttribute("name");
        User user = userService.getUserByEmail(email);

        List<Book> books = userService.getAllBooksAddedByUser(user.getId());
        BookDTOService dtoService = new BookDTOService();

        model.addAttribute("books", dtoService.getBooks(books));
        model.addAttribute("message", email);

        return "user_books";
    }

    @GetMapping("/users/logout")
    public String logout_User(Model model) {

        User user= new User();
        model.addAttribute("loginForm", user);
        model.addAttribute("logout", "You have been logged out");
        return "index";
    }
}
