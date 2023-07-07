package ru.otus.homework.service;

import ru.otus.homework.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

	Optional<Genre> findGenreById(long id);

	List<Genre> findAllGenres();

	Genre createGenre(Genre genre);

	Genre updateGenre(Genre genre);

	int deleteGenreById(long id);

}
