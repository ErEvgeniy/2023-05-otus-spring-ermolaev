package ru.otus.homework.service;

import ru.otus.homework.rest.dto.GenreDto;

import java.util.List;

public interface GenreService {

	GenreDto findGenreById(long id);

	List<GenreDto> findAllGenres();

	GenreDto createGenre(GenreDto genre);

	GenreDto updateGenre(GenreDto genre);

	void deleteGenreById(long id);

}
