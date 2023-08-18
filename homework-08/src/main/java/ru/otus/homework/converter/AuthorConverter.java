package ru.otus.homework.converter;

import org.springframework.stereotype.Component;
import ru.otus.homework.domain.Author;

@Component
public class AuthorConverter {

	public String getAuthorFullName(Author author) {
		return String.format("%s %s %s", author.getLastname(), author.getFirstname(), author.getPatronymic());
	}

	public String getAuthorFullNameWithId(Author author) {
		return String.format("%s - %s", author.getId(), getAuthorFullName(author));
	}
}
