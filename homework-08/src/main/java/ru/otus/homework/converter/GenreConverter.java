package ru.otus.homework.converter;

import org.springframework.stereotype.Component;
import ru.otus.homework.domain.Genre;

@Component
public class GenreConverter {

	public String getGenreWithId(Genre genre) {
		return String.format("%s - %s", genre.getId(), genre.getName());
	}

}
