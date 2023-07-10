package ru.otus.homework.service;

import ru.otus.homework.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

	Optional<Author> findOptionalAuthorById(long id);

	Author findAuthorById(long id);

	List<Author> findAllAuthors();

	Author createAuthor(Author author);

	Author updateAuthor(Author author);

	void deleteAuthorById(long id);

}
