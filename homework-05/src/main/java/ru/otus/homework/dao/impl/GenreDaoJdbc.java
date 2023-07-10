package ru.otus.homework.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.homework.dao.GenreDao;
import ru.otus.homework.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreDaoJdbc implements GenreDao {

	private final NamedParameterJdbcTemplate jdbc;

	@Override
	public Optional<Genre> findById(long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue("GENRE_ID", id);
		try {
			return Optional.ofNullable(jdbc.queryForObject("SELECT GENRE_ID, NAME " +
					"FROM GENRE WHERE GENRE_ID = :GENRE_ID",
				parameters, new GenreMapper()));
		} catch (IncorrectResultSizeDataAccessException ex) {
			return Optional.empty();
		}
	}

	@Override
	public List<Genre> findAll() {
		return jdbc.query("SELECT GENRE_ID, NAME FROM GENRE", new GenreMapper());
	}

	@Override
	public Genre create(Genre genre) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue("NAME", genre.getName());
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbc.update("INSERT INTO GENRE(NAME) VALUES (:NAME)", parameters, keyHolder, new String[]{"GENRE_ID"});

		Objects.requireNonNull(keyHolder.getKey());
		genre.setId(keyHolder.getKey().longValue());
		return genre;
	}

	@Override
	public Genre update(Genre genre) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue("GENRE_ID", genre.getId())
			.addValue("NAME", genre.getName());
		jdbc.update("UPDATE GENRE SET NAME = :NAME WHERE GENRE_ID = :GENRE_ID", parameters);
		return genre;
	}

	@Override
	public void deleteById(long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue("GENRE_ID", id);
		jdbc.update("DELETE FROM GENRE WHERE GENRE_ID = :GENRE_ID", parameters);
	}

	private static class GenreMapper implements RowMapper<Genre> {

		@Override
		public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
			return Genre.builder()
				.id(rs.getLong("GENRE_ID"))
				.name(rs.getString("NAME"))
				.build();
		}

	}

}
