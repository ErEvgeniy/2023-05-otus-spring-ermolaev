package ru.otus.homework.dao;

import ru.otus.homework.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {

	Optional<Book> findById(long id);

	List<Book> findAll();

	Book create(Book book);

	Book update(Book book);

	void deleteById(long id);

}
