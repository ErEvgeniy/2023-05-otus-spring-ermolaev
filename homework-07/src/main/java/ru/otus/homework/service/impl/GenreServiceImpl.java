package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.repository.GenreRepository;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.service.GenreService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

	private final GenreRepository genreRepository;

	@Override
	@Transactional(readOnly = true)
	public Genre findGenreById(long id) {
		Optional<Genre> genreOptional = genreRepository.findById(id);
		return genreOptional.orElseThrow(
			() -> new DataNotFoundException(String.format("Genre with id: %d not found", id)));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Genre> findAllGenres() {
		return genreRepository.findAll();
	}

	@Override
	@Transactional
	public Genre createGenre(Genre genre) {
		return genreRepository.save(genre);
	}

	@Override
	@Transactional
	public Genre updateGenre(Genre genre) {
		Genre toUpdate = genreRepository.findById(genre.getId()).orElseThrow(
			() -> new DataNotFoundException(
				String.format("Genre with id: %s not found", genre.getId())));
		String newGenreName = genre.getName();
		if (newGenreName != null && !newGenreName.isEmpty()) {
			toUpdate.setName(newGenreName);
		}
		return genreRepository.save(toUpdate);
	}

	@Override
	@Transactional
	public void deleteGenreById(long id) {
		genreRepository.deleteById(id);
	}

}
