package ru.otus.homework.service;

import ru.otus.homework.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

	Optional<Book> findOptionalBookById(long id);

	Book findBookById(long id);

	List<Book> findAllBooks();

	Book createBook(Book book);

	Book updateBook(Book book);

	void deleteBookById(long id);

}
