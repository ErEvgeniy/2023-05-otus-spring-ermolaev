package ru.otus.homework.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.homework.dao.BookDao;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {

	@Override
	public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
		Genre genre = Genre.builder()
			.id(rs.getLong(BookDao.GENRE_ID_COLUMN))
			.name(rs.getString(BookDao.GENRE_NAME_COLUMN))
			.build();

		Author author = Author.builder()
			.id(rs.getLong(BookDao.AUTHOR_ID_COLUMN))
			.firstname(rs.getString(BookDao.AUTHOR_FIRSTNAME_COLUMN))
			.patronymic(rs.getString(BookDao.AUTHOR_PATRONYMIC_COLUMN))
			.lastname(rs.getString(BookDao.AUTHOR_LASTNAME_COLUMN))
			.build();

		return Book.builder()
			.id(rs.getLong(BookDao.ID_COLUMN))
			.name(rs.getString(BookDao.NAME_COLUMN))
			.genre(genre)
			.author(author)
			.build();
	}

}
