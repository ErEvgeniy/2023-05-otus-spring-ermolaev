package ru.otus.homework.service;

import ru.otus.homework.domain.Book;

import java.util.List;

public interface BookService {

	Book findBookById(long id);

	List<Book> findAllBooks();

	Book createBook(Book book);

	Book updateBook(Book book);

	void deleteBookById(long id);

}
