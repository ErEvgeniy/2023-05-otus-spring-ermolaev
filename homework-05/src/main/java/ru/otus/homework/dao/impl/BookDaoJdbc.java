package ru.otus.homework.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.homework.dao.BookDao;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {

	private final NamedParameterJdbcTemplate jdbc;

	@Override
	public Optional<Book> findById(long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue("BOOK_ID", id);
		try {
			return Optional.ofNullable(jdbc.queryForObject("SELECT b.BOOK_ID, b.NAME, \n" +
				"g.GENRE_ID as GENRE_ID, g.NAME as GENRE_NAME, \n" +
				"a.AUTHOR_ID as AUTHOR_ID, a.FIRSTNAME as AUTHOR_FIRSTNAME, \n" +
				"a.PATRONYMIC as AUTHOR_PATRONYMIC, a.LASTNAME as AUTHOR_LASTNAME \n" +
				"FROM BOOK b \n" +
				"JOIN GENRE g ON b.GENRE_ID = g.GENRE_ID \n" +
				"JOIN AUTHOR a ON b.AUTHOR_ID = a.AUTHOR_ID \n" +
				"WHERE b.BOOK_ID = :BOOK_ID", parameters, new BookMapper()));
		} catch (IncorrectResultSizeDataAccessException ex) {
			return Optional.empty();
		}
	}

	@Override
	public List<Book> findAll() {
		return jdbc.query("SELECT b.BOOK_ID, b.NAME, \n" +
			"g.GENRE_ID as GENRE_ID, g.NAME as GENRE_NAME, \n" +
			"a.AUTHOR_ID as AUTHOR_ID, a.FIRSTNAME as AUTHOR_FIRSTNAME, \n" +
			"a.PATRONYMIC as AUTHOR_PATRONYMIC, a.LASTNAME as AUTHOR_LASTNAME \n" +
			"FROM BOOK b \n" +
			"JOIN GENRE g ON b.GENRE_ID = g.GENRE_ID \n" +
			"JOIN AUTHOR a ON b.AUTHOR_ID = a.AUTHOR_ID", new BookMapper());
	}

	@Override
	public Book create(Book book) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue("NAME", book.getName())
			.addValue("GENRE_ID", book.getGenre().getId())
			.addValue("AUTHOR_ID", book.getAuthor().getId());
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbc.update("INSERT INTO BOOK(NAME, GENRE_ID, AUTHOR_ID) VALUES (:NAME, :GENRE_ID, :AUTHOR_ID)",
			parameters, keyHolder, new String[]{"BOOK_ID"});

		Objects.requireNonNull(keyHolder.getKey());
		book.setId(keyHolder.getKey().longValue());
		return book;
	}

	@Override
	public Book update(Book book) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue("BOOK_ID", book.getId())
			.addValue("NAME", book.getName())
			.addValue("GENRE_ID", book.getGenre().getId())
			.addValue("AUTHOR_ID", book.getAuthor().getId());
		jdbc.update("UPDATE BOOK SET NAME = :NAME, GENRE_ID = :GENRE_ID, AUTHOR_ID = :AUTHOR_ID " +
			"WHERE BOOK_ID = :BOOK_ID", parameters);
		return book;
	}

	@Override
	public void deleteById(long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource()
			.addValue("BOOK_ID", id);
		jdbc.update("DELETE FROM BOOK WHERE BOOK_ID = :BOOK_ID", parameters);
	}

	private static class BookMapper implements RowMapper<Book> {

		@Override
		public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
			Genre genre = Genre.builder()
				.id(rs.getLong("GENRE_ID"))
				.name(rs.getString("GENRE_NAME"))
				.build();

			Author author = Author.builder()
				.id(rs.getLong("AUTHOR_ID"))
				.firstname(rs.getString("AUTHOR_FIRSTNAME"))
				.patronymic(rs.getString("AUTHOR_PATRONYMIC"))
				.lastname(rs.getString("AUTHOR_LASTNAME"))
				.build();

			return Book.builder()
				.id(rs.getLong("BOOK_ID"))
				.name(rs.getString("NAME"))
				.genre(genre)
				.author(author)
				.build();
		}

	}

}
