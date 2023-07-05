package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.dao.GenreDao;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.service.GenreService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

	private final GenreDao genreDao;

	@Override
	public Optional<Genre> findGenreById(long id) {
		return genreDao.findById(id);
	}

	@Override
	public List<Genre> findAllGenres() {
		return genreDao.findAll();
	}

	@Override
	@Transactional
	public Genre createGenre(Genre genre) {
		return genreDao.create(genre);
	}

	@Override
	@Transactional
	public Genre updateGenre(Genre genre) {
		return genreDao.update(genre);
	}

	@Override
	@Transactional
	public int deleteGenreById(long id) {
		return genreDao.deleteById(id);
	}

}
