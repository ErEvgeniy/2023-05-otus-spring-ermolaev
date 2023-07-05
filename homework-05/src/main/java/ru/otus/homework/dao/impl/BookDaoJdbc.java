package ru.otus.homework.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.homework.dao.BookDao;
import ru.otus.homework.dao.mapper.BookMapper;
import ru.otus.homework.domain.Book;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {

	private final NamedParameterJdbcTemplate jdbc;

	@Override
	public Optional<Book> findById(long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue(ID_COLUMN, id);
		try {
			return Optional.ofNullable(jdbc.queryForObject("SELECT b.ID, b.NAME, \n" +
				"g.ID as GENRE_ID, g.NAME as GENRE_NAME, \n" +
				"a.ID as AUTHOR_ID, a.FIRSTNAME as AUTHOR_FIRSTNAME, \n" +
				"a.PATRONYMIC as AUTHOR_PATRONYMIC, a.LASTNAME as AUTHOR_LASTNAME \n" +
				"FROM BOOK b \n" +
				"JOIN GENRE g ON b.GENRE = g.ID \n" +
				"JOIN AUTHOR a ON b.AUTHOR = a.ID \n" +
				"WHERE b.ID = :ID", parameters, new BookMapper()));
		} catch (IncorrectResultSizeDataAccessException ex) {
			return Optional.empty();
		}
	}

	@Override
	public List<Book> findAll() {
		return jdbc.query("SELECT b.ID, b.NAME, \n" +
			"g.ID as GENRE_ID, g.NAME as GENRE_NAME, \n" +
			"a.ID as AUTHOR_ID, a.FIRSTNAME as AUTHOR_FIRSTNAME, \n" +
			"a.PATRONYMIC as AUTHOR_PATRONYMIC, a.LASTNAME as AUTHOR_LASTNAME \n" +
			"FROM BOOK b \n" +
			"JOIN GENRE g ON b.GENRE = g.ID \n" +
			"JOIN AUTHOR a ON b.AUTHOR = a.ID", new BookMapper());
	}

	@Override
	public Book create(Book book) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue(NAME_COLUMN, book.getName())
			.addValue(GENRE_COLUMN, book.getGenre().getId())
			.addValue(AUTHOR_COLUMN, book.getAuthor().getId());
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbc.update("INSERT INTO BOOK(NAME, GENRE, AUTHOR) VALUES (:NAME, :GENRE, :AUTHOR)",
			parameters, keyHolder, new String[]{"ID"});

		Optional.ofNullable(keyHolder.getKey()).ifPresent(key -> book.setId(key.longValue()));
		return book;
	}

	@Override
	public Book update(Book book) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue(ID_COLUMN, book.getId())
			.addValue(NAME_COLUMN, book.getName())
			.addValue(GENRE_COLUMN, book.getGenre().getId())
			.addValue(AUTHOR_COLUMN, book.getAuthor().getId());
		jdbc.update("UPDATE BOOK SET NAME = :NAME WHERE ID = :ID", parameters);
		return book;
	}

	@Override
	public int deleteById(long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue(ID_COLUMN, id);
		return jdbc.update("DELETE FROM BOOK WHERE ID = :ID", parameters);
	}

}
