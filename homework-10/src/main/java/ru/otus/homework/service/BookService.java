package ru.otus.homework.service;

import ru.otus.homework.rest.dto.BookDto;

import java.util.List;

public interface BookService {

	BookDto findBookById(long id);

	List<BookDto> findAllBooks();

	BookDto createBook(BookDto book);

	BookDto updateBook(BookDto book);

	void deleteBookById(long id);

}
