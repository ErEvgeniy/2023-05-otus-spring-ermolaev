package ru.otus.homework.service;

import ru.otus.homework.domain.Author;

import java.util.List;

public interface AuthorService {

	Author findAuthorById(long id);

	List<Author> findAllAuthors();

	Author createAuthor(Author author);

	Author updateAuthor(Author author);

	void deleteAuthorById(long id);

}
