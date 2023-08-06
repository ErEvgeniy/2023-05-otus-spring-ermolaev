package ru.otus.homework.service;

import ru.otus.homework.domain.Genre;

import java.util.List;

public interface GenreService {

	Genre findGenreById(String id);

	List<Genre> findAllGenres();

	Genre createGenre(Genre genre);

	Genre updateGenre(Genre genre);

	void deleteGenreById(String id);

}
