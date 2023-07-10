package ru.otus.homework.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.homework.dao.AuthorDao;
import ru.otus.homework.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {

	private final NamedParameterJdbcTemplate jdbc;

	@Override
	public Optional<Author> findById(long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue("AUTHOR_ID", id);
		try {
			return Optional.ofNullable(jdbc.queryForObject("SELECT AUTHOR_ID, FIRSTNAME, " +
				"PATRONYMIC, LASTNAME " +
				"FROM AUTHOR WHERE AUTHOR_ID = :AUTHOR_ID", parameters, new AuthorMapper()));
		} catch (IncorrectResultSizeDataAccessException ex) {
			return Optional.empty();
		}
	}

	@Override
	public List<Author> findAll() {
		return jdbc.query("SELECT AUTHOR_ID, FIRSTNAME, PATRONYMIC, LASTNAME FROM AUTHOR", new AuthorMapper());
	}

	@Override
	public Author create(Author author) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue("FIRSTNAME", author.getFirstname())
			.addValue("PATRONYMIC", author.getPatronymic())
			.addValue("LASTNAME", author.getLastname());
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbc.update("INSERT INTO AUTHOR(FIRSTNAME, PATRONYMIC, LASTNAME) \n" +
				"VALUES (:FIRSTNAME, :PATRONYMIC, :LASTNAME)", parameters,
			keyHolder, new String[]{"AUTHOR_ID"});

		Objects.requireNonNull(keyHolder.getKey());
		author.setId(keyHolder.getKey().longValue());
		return author;
	}

	@Override
	public Author update(Author author) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue("AUTHOR_ID", author.getId())
			.addValue("FIRSTNAME", author.getFirstname())
			.addValue("PATRONYMIC", author.getPatronymic())
			.addValue("LASTNAME", author.getLastname());
		jdbc.update("UPDATE AUTHOR SET FIRSTNAME = :FIRSTNAME, \n" +
			"PATRONYMIC = :PATRONYMIC, \n" +
			"LASTNAME = :LASTNAME \n" +
			"WHERE AUTHOR_ID = :AUTHOR_ID", parameters);
		return author;
	}

	@Override
	public void deleteById(long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue("AUTHOR_ID", id);
		jdbc.update("DELETE FROM AUTHOR WHERE AUTHOR_ID = :AUTHOR_ID", parameters);
	}

	private static class AuthorMapper implements RowMapper<Author> {

		@Override
		public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
			return Author.builder()
				.id(rs.getLong("AUTHOR_ID"))
				.firstname(rs.getString("FIRSTNAME"))
				.patronymic(rs.getString("PATRONYMIC"))
				.lastname(rs.getString("LASTNAME"))
				.build();
		}

	}

}
