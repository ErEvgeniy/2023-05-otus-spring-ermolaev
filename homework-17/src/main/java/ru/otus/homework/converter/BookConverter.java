package ru.otus.homework.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.Book;

@Component
@RequiredArgsConstructor
public class BookConverter {

	private final AuthorConverter authorConverter;

	public String getBookNameWithId(Book book) {
		return String.format("%s - %s", book.getId(), book.getName());
	}

	public String getBookNameWithIdAndGenre(Book book) {
		return String.format("%s; GENRE: %s", getBookNameWithId(book), book.getGenre().getName());
	}

	public String getBookNameWithIdAndGenreAndAuthor(Book book) {
		return String.format("%s; AUTHOR: %s",
			getBookNameWithIdAndGenre(book),
			authorConverter.getAuthorFullName(book.getAuthor())
		);
	}

}
