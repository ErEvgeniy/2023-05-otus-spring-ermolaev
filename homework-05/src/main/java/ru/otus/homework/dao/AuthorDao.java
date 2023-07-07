package ru.otus.homework.dao;

import ru.otus.homework.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {

	String ID_COLUMN = "ID";

	String FIRSTNAME_COLUMN = "FIRSTNAME";

	String PATRONYMIC_COLUMN = "PATRONYMIC";

	String LASTNAME_COLUMN = "LASTNAME";

	Optional<Author> findById(long id);

	List<Author> findAll();

	Author create(Author author);

	Author update(Author author);

	int deleteById(long id);

}
