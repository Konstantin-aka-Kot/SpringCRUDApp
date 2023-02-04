package ru.akshentsev.ebook.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.akshentsev.ebook.models.Book;
import ru.akshentsev.ebook.models.Person;

import java.util.List;
@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByOwner(Person owner);

    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String name, String author);
}
