package ru.akshentsev.ebook.controllers;

import ru.akshentsev.ebook.dao.PersonDAO;
import ru.akshentsev.ebook.services.BooksService;
import ru.akshentsev.ebook.services.PeopleService;
import ru.akshentsev.ebook.util.PersonValidator;
import ru.akshentsev.ebook.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    private final BooksService booksService;
    private final PersonDAO personDAO;
    private final PersonValidator personValidator;

    private static final String REDIRECTTOPEOPLE = "redirect:/people";

    @Autowired
    public PeopleController(PeopleService peopleService, BooksService booksService, PersonDAO personDAO, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.personDAO = personDAO;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String index(Model model) {
        //получим всех людей из DAO и передадим на отображение в представление
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findOne(id));
        model.addAttribute("books", booksService.findByOwner(peopleService.findOne(id)));
        return "people/show";
    }
    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }
    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) return "people/new";
        peopleService.save(person);
        return REDIRECTTOPEOPLE;
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", peopleService.findOne(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "people/edit";
        peopleService.update(id, person);
        return REDIRECTTOPEOPLE;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return REDIRECTTOPEOPLE;
    }
}
