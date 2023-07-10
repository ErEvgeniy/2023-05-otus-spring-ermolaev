package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.dao.GenreDao;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.service.GenreService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

	private final GenreDao genreDao;

	@Override
	@Transactional(readOnly = true)
	public Optional<Genre> findOptionalGenreById(long id) {
		return genreDao.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Genre findGenreById(long id) {
		return getGenre(id);
	}

	private Genre getGenre(long id) {
		Optional<Genre> genreOptional = findOptionalGenreById(id);
		return genreOptional.orElseThrow(
			() -> new DataNotFoundException(String.format("Genre with id: %d not found", id)));
	}

	@Override
	@Transactional(readOnly = true)
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
		Genre toUpdate = getGenre(genre.getId());
		String newGenreName = genre.getName();
		if (newGenreName != null && !newGenreName.isEmpty()) {
			toUpdate.setName(newGenreName);
		}
		return genreDao.update(toUpdate);
	}

	@Override
	@Transactional
	public void deleteGenreById(long id) {
		genreDao.deleteById(id);
	}

}
