package ru.otus.homework.dao;

import ru.otus.homework.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {

	Optional<Author> findById(long id);

	List<Author> findAll();

	Author create(Author author);

	Author update(Author author);

	void deleteById(long id);

}
