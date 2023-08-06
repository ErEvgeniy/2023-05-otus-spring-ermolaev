package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
	public Genre findGenreById(String id) {
		Optional<Genre> genreOptional = genreRepository.findById(id);
		return genreOptional.orElseThrow(
			() -> new DataNotFoundException(String.format("Genre with id: %s not found", id)));
	}

	@Override
	public List<Genre> findAllGenres() {
		return genreRepository.findAll();
	}

	@Override
	public Genre createGenre(Genre genre) {
		return genreRepository.save(genre);
	}

	@Override
	public Genre updateGenre(Genre genre) {
		Genre toUpdate = findGenreById(genre.getId());
		String newGenreName = genre.getName();
		if (newGenreName != null && !newGenreName.isEmpty()) {
			toUpdate.setName(newGenreName);
		}
		return genreRepository.save(toUpdate);
	}

	@Override
	public void deleteGenreById(String id) {
		genreRepository.deleteById(id);
	}

}
