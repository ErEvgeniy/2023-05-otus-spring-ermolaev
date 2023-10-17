package ru.otus.homework.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.converter.BookConverter;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.service.BookService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class BookShellCommands {

	private final BookService bookService;

	private final BookConverter bookConverter;

	@ShellMethod(value = "Print book list", key = {"bl", "book-list"})
	public String bookList() {
		List<Book> books = bookService.findAllBooks();
		StringBuilder builder = new StringBuilder("Existed books:").append(System.lineSeparator());
		for (Book book : books) {
			builder
				.append(bookConverter.getBookNameWithIdAndGenreAndAuthor(book))
				.append(System.lineSeparator());
		}
		return builder.toString();
	}

	@ShellMethod(value = "Print book by id", key = {"b", "book-by-id"})
	public String bookById(@ShellOption String bookId) {
		try {
			Book book = bookService.findBookById(bookId);
			return bookConverter.getBookNameWithIdAndGenreAndAuthor(book);
		} catch (DataNotFoundException ex) {
			return ex.getMessage();
		}
	}

	@ShellMethod(value = "Delete book by id", key = {"bd", "book-delete"})
	public String bookDelete(@ShellOption String bookId) {
		try {
			bookService.deleteBookById(bookId);
			return String.format("Book with id: %s successfully deleted", bookId);
		} catch (DataIntegrityViolationException ex) {
			return String.format("Book with id: %s is used. Unable to delete", bookId);
		}
	}

	@ShellMethod(value = "Create new book", key = {"bc", "book-create"})
	public String bookCreate(
		@ShellOption(value = {"-n", "--name"}) String name,
		@ShellOption(value = {"-a", "--authorId"}) String authorId,
		@ShellOption(value = {"-g", "--genreId"}) String genreId
	) {
		Book book = Book.builder()
			.name(name)
			.author(new Author().setId(authorId))
			.genre(new Genre().setId(genreId))
			.build();

		Book createdBook;
		try {
			createdBook = bookService.createBook(book);
		} catch (DataNotFoundException ex) {
			return ex.getMessage();
		}

		return String.format("Book created: %s",
			bookConverter.getBookNameWithIdAndGenreAndAuthor(createdBook));
	}

	@ShellMethod(value = "Update existed book", key = {"bu", "book-update"})
	public String bookUpdate(
		@ShellOption(value = {"-i", "--bookId"}) String bookId,
		@ShellOption(defaultValue = ShellOption.NULL, value = {"-n", "--name"}) String newName,
		@ShellOption(defaultValue = ShellOption.NULL, value = {"-a", "--authorId"}) String newAuthorId,
		@ShellOption(defaultValue = ShellOption.NULL, value = {"-g", "--genreId"}) String newGenreId
	) {
		Book book = Book.builder()
			.id(bookId)
			.name(newName)
			.author(new Author().setId(newAuthorId))
			.genre(new Genre().setId(newGenreId))
			.build();

		Book updatedBook = bookService.updateBook(book);
		return String.format("Book updated: %s", bookConverter.getBookNameWithIdAndGenreAndAuthor(updatedBook));
	}

}
