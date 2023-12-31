package ru.otus.homework.repository;

import ru.otus.homework.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends Repository<Book> {

	List<Book> findAll();

	Optional<Book> findById(Long id);

}
