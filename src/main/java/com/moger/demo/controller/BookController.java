package com.moger.demo.controller;

import com.moger.demo.DTOs.BookDTO;
import com.moger.demo.config.AppConfig;
import com.moger.demo.dataconstants.BookGenre;
import com.moger.demo.entities.*;
import com.moger.demo.service.BookService;
import com.moger.demo.customValidators.BookValidator;
import com.moger.demo.service.UserService;
import com.moger.demo.serviceImp.BookDTOService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Validated
@Controller
public class BookController {

	@Autowired
	private AppConfig appConfig;

	@InitBinder("book")
	protected void initBookBinder(WebDataBinder binder) {
		binder.setValidator(new BookValidator());
	}

	String email;
	private BookService service;
	private UserService userService;

	// Temporary in-memory storage - for bookmarking books
	private final Map<Long, BookDTO> bookmarks = new ConcurrentHashMap<>();

	public BookController(BookService service, UserService userService, AppConfig appConfig) {
		this.service = service;
		this.userService = userService;
		this.appConfig = appConfig;
	}

	@GetMapping("/books/new")
	public String add_Book(Model model, HttpSession session) {

		String email = (String) session.getAttribute("name");
		//create object to hold data
		Book book= new Book();
		model.addAttribute("book", book);
		model.addAttribute("message", email);

		return "add_book";
	}

	//saving book into book and bookUser
	@PostMapping("/books/new")
	public String save_added_Book(@Valid @ModelAttribute("book") Book book, BindingResult result, HttpSession session) {

        if(result.hasErrors())
            return "redirect:/books/new";

		String name = book.getGenre().getName().toUpperCase();
		book.setGenre(BookGenre.valueOf(name));

		String email = (String) session.getAttribute("name");

		User user = userService.getUserByEmail(email);

		service.saveBook(book);
		service.saveBookUser(book, user);

		return  "redirect:/books";
	}

	@GetMapping("/books")
	public String getAllBooks(Model model) {

		List<Book> books = service.getAllBooks();

		BookDTOService dtoService = new BookDTOService();
		model.addAttribute("books", dtoService.getBooks(books));

		model.addAttribute("message", email);
		return "book";
	}

	@GetMapping("/books/{id}")
	public String update_Book(@PathVariable("id") Long id, Model model) {

		model.addAttribute("book",service.getBookById(id));
		return "update";
	}

	@PostMapping("/books/{id}")
	public String save_updated_Book(@Valid @ModelAttribute("book") Book book, @PathVariable("id") Long id, BindingResult result) {

        if(result.hasErrors()) {
			return "redirect:/books/{id}";
		}

		try {
			service.updateBook(book, id);
		}catch (ValidationException e){
			e.getMessage();
		}
		return "redirect:/books";
	}

	@GetMapping("/books/resource/{id}")
	public String delete_Book(@PathVariable("id") Long id, Model model) {

		int result = service.deleteBook(id);
        if(result > 0) {
			bookmarks.remove(id);
			return "redirect:/books";
		}
        model.addAttribute("error", "There are no books in the books library. Please add one.");
        return "add-book";
	}

	// Bookmarking
	@GetMapping("/books/bookmark/{id}")
	public String bookmark_Book(@PathVariable("id") Long id, Model model) {

		Book book = service.saveBooks(id);

		BookDTOService dtoService = new BookDTOService();
		bookmarks.put(id, dtoService.getBook(book));
		model.addAttribute("books", bookmarks.values());

		return "savedBooks";
	}

	@GetMapping("/books/bookmarks")
	public String bookmarked_ByUser(Model model) {

		model.addAttribute("books", bookmarks.values());
		return "savedBooks";
	}
}




