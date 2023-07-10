package ru.otus.homework.dao;

import ru.otus.homework.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {

	Optional<Genre> findById(long id);

	List<Genre> findAll();

	Genre create(Genre genre);

	Genre update(Genre genre);

	void deleteById(long id);

}
