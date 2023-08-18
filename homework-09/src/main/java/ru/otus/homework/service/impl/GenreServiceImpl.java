package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.dto.GenreDto;
import ru.otus.homework.mapper.GenreMapper;
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

	private final GenreMapper genreMapper;

	@Override
	@Transactional(readOnly = true)
	public GenreDto findGenreById(long id) {
		Optional<Genre> genreOptional = genreRepository.findById(id);
		if (genreOptional.isEmpty()) {
			throw new DataNotFoundException(String.format("Genre with id: %d not found", id));
		}
		return genreMapper.toDto(genreOptional.get());
	}

	@Override
	@Transactional(readOnly = true)
	public List<GenreDto> findAllGenres() {
		List<Genre> genres = genreRepository.findAll();
		return genreMapper.toDtoList(genres);
	}

	@Override
	@Transactional
	public GenreDto createGenre(GenreDto genre) {
		Genre newGenre = genreMapper.toDomain(genre);
		genreRepository.save(newGenre);
		return genreMapper.toDto(newGenre);
	}

	@Override
	@Transactional
	public GenreDto updateGenre(GenreDto genre) {
		Genre toUpdate = genreRepository.findById(genre.getId()).orElseThrow(
			() -> new DataNotFoundException(
				String.format("Genre with id: %s not found", genre.getId())));
		String newGenreName = genre.getName();
		if (newGenreName != null && !newGenreName.isEmpty()) {
			toUpdate.setName(newGenreName);
		}
		genreRepository.save(toUpdate);
		return genreMapper.toDto(toUpdate);
	}

	@Override
	@Transactional
	public void deleteGenreById(long id) {
		genreRepository.deleteById(id);
	}

}
