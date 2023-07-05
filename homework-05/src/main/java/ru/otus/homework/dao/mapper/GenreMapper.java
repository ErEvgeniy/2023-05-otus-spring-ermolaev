package ru.otus.homework.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.homework.dao.GenreDao;
import ru.otus.homework.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreMapper implements RowMapper<Genre> {

	@Override
	public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
		return Genre.builder()
			.id(rs.getLong(GenreDao.ID_COLUMN))
			.name(rs.getString(GenreDao.NAME_COLUMN))
			.build();
	}

}
