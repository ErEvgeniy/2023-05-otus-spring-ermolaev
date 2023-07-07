package ru.otus.homework.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.homework.dao.AuthorDao;
import ru.otus.homework.dao.mapper.AuthorMapper;
import ru.otus.homework.domain.Author;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {

	private final NamedParameterJdbcTemplate jdbc;

	@Override
	public Optional<Author> findById(long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue(ID_COLUMN, id);
		try {
			return Optional.ofNullable(jdbc.queryForObject("SELECT ID, FIRSTNAME, PATRONYMIC, LASTNAME \n" +
				"FROM AUTHOR WHERE ID = :ID", parameters, new AuthorMapper()));
		} catch (IncorrectResultSizeDataAccessException ex) {
			return Optional.empty();
		}
	}

	@Override
	public List<Author> findAll() {
		return jdbc.query("SELECT ID, FIRSTNAME, PATRONYMIC, LASTNAME FROM AUTHOR", new AuthorMapper());
	}

	@Override
	public Author create(Author author) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue(FIRSTNAME_COLUMN, author.getFirstname())
			.addValue(PATRONYMIC_COLUMN, author.getPatronymic())
			.addValue(LASTNAME_COLUMN, author.getLastname());
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbc.update("INSERT INTO AUTHOR(FIRSTNAME, PATRONYMIC, LASTNAME) \n" +
				"VALUES (:FIRSTNAME, :PATRONYMIC, :LASTNAME)", parameters,
			keyHolder, new String[]{"ID"});

		Optional.ofNullable(keyHolder.getKey()).ifPresent(key -> author.setId(key.longValue()));
		return author;
	}

	@Override
	public Author update(Author author) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue(ID_COLUMN, author.getId())
			.addValue(FIRSTNAME_COLUMN, author.getFirstname())
			.addValue(PATRONYMIC_COLUMN, author.getPatronymic())
			.addValue(LASTNAME_COLUMN, author.getLastname());
		jdbc.update("UPDATE AUTHOR SET FIRSTNAME = :FIRSTNAME, \n" +
			"PATRONYMIC = :PATRONYMIC, \n" +
			"LASTNAME = :LASTNAME \n" +
			"WHERE ID = :ID", parameters);
		return author;
	}

	@Override
	public int deleteById(long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue(ID_COLUMN, id);
		return jdbc.update("DELETE FROM AUTHOR WHERE ID = :ID", parameters);
	}

}
