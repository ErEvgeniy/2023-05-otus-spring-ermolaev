package ru.otus.homework.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.homework.dao.AuthorDao;
import ru.otus.homework.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorMapper implements RowMapper<Author> {

	@Override
	public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
		return Author.builder()
			.id(rs.getLong(AuthorDao.ID_COLUMN))
			.firstname(rs.getString(AuthorDao.FIRSTNAME_COLUMN))
			.patronymic(rs.getString(AuthorDao.PATRONYMIC_COLUMN))
			.lastname(rs.getString(AuthorDao.LASTNAME_COLUMN))
			.build();
	}

}
