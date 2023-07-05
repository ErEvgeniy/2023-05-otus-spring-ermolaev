package ru.otus.homework.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.homework.dao.GenreDao;
import ru.otus.homework.dao.mapper.GenreMapper;
import ru.otus.homework.domain.Genre;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreDaoJdbc implements GenreDao {

	private final NamedParameterJdbcTemplate jdbc;

	@Override
	public Optional<Genre> findById(long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue(ID_COLUMN, id);
		try {
			return Optional.ofNullable(jdbc.queryForObject("SELECT ID, NAME FROM GENRE WHERE ID = :ID",
				parameters, new GenreMapper()));
		} catch (IncorrectResultSizeDataAccessException ex) {
			return Optional.empty();
		}
	}

	@Override
	public List<Genre> findAll() {
		return jdbc.query("SELECT ID, NAME FROM GENRE", new GenreMapper());
	}

	@Override
	public Genre create(Genre genre) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue(NAME_COLUMN, genre.getName());
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbc.update("INSERT INTO GENRE(NAME) VALUES (:NAME)", parameters, keyHolder, new String[]{"ID"});

		Optional.ofNullable(keyHolder.getKey()).ifPresent(key -> genre.setId(key.longValue()));
		return genre;
	}

	@Override
	public Genre update(Genre genre) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue(ID_COLUMN, genre.getId())
			.addValue(NAME_COLUMN, genre.getName());
		jdbc.update("UPDATE GENRE SET NAME = :NAME WHERE ID = :ID", parameters);
		return genre;
	}

	@Override
	public int deleteById(long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue(ID_COLUMN, id);
		return jdbc.update("DELETE FROM GENRE WHERE ID = :ID", parameters);
	}

}
