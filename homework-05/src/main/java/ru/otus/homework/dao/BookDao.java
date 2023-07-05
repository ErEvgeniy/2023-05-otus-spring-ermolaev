package ru.otus.homework.dao;

import ru.otus.homework.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {

	String ID_COLUMN = "ID";

	String NAME_COLUMN = "NAME";

	String GENRE_COLUMN = "GENRE";

	String AUTHOR_COLUMN = "AUTHOR";

	String GENRE_ID_COLUMN = "GENRE_ID";

	String GENRE_NAME_COLUMN = "GENRE_NAME";

	String AUTHOR_ID_COLUMN = "AUTHOR_ID";

	String AUTHOR_FIRSTNAME_COLUMN = "AUTHOR_FIRSTNAME";

	String AUTHOR_PATRONYMIC_COLUMN = "AUTHOR_PATRONYMIC";

	String AUTHOR_LASTNAME_COLUMN = "AUTHOR_LASTNAME";

	Optional<Book> findById(long id);

	List<Book> findAll();

	Book create(Book book);

	Book update(Book book);

	int deleteById(long id);

}
