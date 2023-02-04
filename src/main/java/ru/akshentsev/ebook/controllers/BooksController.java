package ru.akshentsev.ebook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.akshentsev.ebook.dao.BookDAO;
import ru.akshentsev.ebook.dao.PersonDAO;
import ru.akshentsev.ebook.models.Book;
import ru.akshentsev.ebook.models.Person;
import ru.akshentsev.ebook.services.BooksService;
import ru.akshentsev.ebook.services.PeopleService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    private final BooksService booksService;

    private final PeopleService peopleService;
    private static final String REDIRECTTOBOOKS = "redirect:/books";

    @Autowired
    public BooksController(BookDAO bookDAO, PersonDAO personDAO, BooksService booksService, PeopleService peopleService) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "books_per_page", defaultValue = "10") int booksPerPage,
                        @RequestParam(name = "sort", defaultValue = "year") String sort) {

        Pageable pageable = PageRequest.of(page, booksPerPage, Sort.by(sort).ascending());
        Page<Book> bookPage = booksService.findAll(pageable);
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("page", page);
        model.addAttribute("books_per_page", booksPerPage);
        model.addAttribute("sort", sort);
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findOne(id));
        Person bookOwner = peopleService.findOne(id);

        if (bookOwner != null) model.addAttribute("owner", bookOwner);
        else model.addAttribute("people", booksService.findAll());
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book")Book book) {return "books/new";}

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "books/new";
        booksService.save(book);
        return REDIRECTTOBOOKS;
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) return "books/edit";
        booksService.update(id, book);
        return REDIRECTTOBOOKS;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return REDIRECTTOBOOKS;
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        booksService.release(id);
        return REDIRECTTOBOOKS + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        booksService.assign(id, selectedPerson);
        return REDIRECTTOBOOKS + id;
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam(defaultValue="") String query) {
        List<Book> books = booksService.searchBooks(query);
        model.addAttribute("books", books);
        return "books/search";
    }

}
