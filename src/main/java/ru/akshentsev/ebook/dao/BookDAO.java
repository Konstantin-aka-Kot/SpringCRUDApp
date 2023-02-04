package ru.akshentsev.ebook.dao;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.akshentsev.ebook.models.Book;
import ru.akshentsev.ebook.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final EntityManager entityManager;
    @Autowired
    public BookDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
