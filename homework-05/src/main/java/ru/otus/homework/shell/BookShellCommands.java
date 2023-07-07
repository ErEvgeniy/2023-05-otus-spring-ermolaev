package ru.otus.homework.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.converter.BookConverter;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.service.AuthorService;
import ru.otus.homework.service.BookService;
import ru.otus.homework.service.GenreService;

import java.util.List;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class BookShellCommands {

	private final BookService bookService;

	private final BookConverter bookConverter;

	private final AuthorService authorService;

	private final GenreService genreService;

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
	public String bookById(@ShellOption long bookId) {
		Optional<Book> book = bookService.findBookById(bookId);
		if (book.isPresent()) {
			return bookConverter.getBookNameWithIdAndGenreAndAuthor(book.get());
		} else {
			return String.format("Book with id: %d not found", bookId);
		}
	}

	@ShellMethod(value = "Delete book by id", key = {"bd", "book-delete"})
	public String bookDelete(@ShellOption long bookId) {
		int deletedRows = bookService.deleteBookById(bookId);
		if (deletedRows > 0) {
			return String.format("Book with id: %d successfully deleted", bookId);
		} else {
			return String.format("Book with id: %d was not found to delete", bookId);
		}
	}

	@ShellMethod(value = "Create new book", key = {"bc", "book-create"})
	public String bookCreate(
		@ShellOption(value = {"-n", "--name"}) String name,
		@ShellOption(value = {"-a", "--authorId"}) int authorId,
		@ShellOption(value = {"-g", "--genreId"}) int genreId
	) {
		Optional<Author> author = authorService.findAuthorById(authorId);
		if (author.isEmpty()) {
			return String.format("Author with id: %d was not found", authorId);
		}
		Optional<Genre> genre = genreService.findGenreById(genreId);
		if (genre.isEmpty()) {
			return String.format("Genre with id: %d was not found", genreId);
		}
		Book book = Book.builder()
			.name(name)
			.author(author.get())
			.genre(genre.get())
			.build();
		Book createdGenre = bookService.createBook(book);
		return String.format("Book created: %s",
			bookConverter.getBookNameWithIdAndGenreAndAuthor(createdGenre));
	}

	@ShellMethod(value = "Update existed book", key = {"bu", "book-update"})
	public String bookUpdate(
		@ShellOption(value = {"-i", "--bookId"}) int bookId,
		@ShellOption(defaultValue = ShellOption.NULL, value = {"-n", "--name"}) String newName,
		@ShellOption(defaultValue = ShellOption.NULL, value = {"-a", "--authorId"}) Integer newAuthorId,
		@ShellOption(defaultValue = ShellOption.NULL, value = {"-g", "--genreId"}) Integer newGenreId
	) {
		Optional<Book> existedBook = bookService.findBookById(bookId);
		if (existedBook.isEmpty()) {
			return String.format("Book with id: %d was not found", bookId);
		}
		Book bookToUpdate = existedBook.get();
		if (newAuthorId != null) {
			Optional<Author> author = authorService.findAuthorById(newAuthorId);
			if (author.isEmpty()) {
				return String.format("Author with id: %d was not found", newAuthorId);
			}
			author.ifPresent(bookToUpdate::setAuthor);
		}
		if (newGenreId != null) {
			Optional<Genre> genre = genreService.findGenreById(newGenreId);
			if (genre.isEmpty()) {
				return String.format("Genre with id: %d was not found", newGenreId);
			}
			genre.ifPresent(bookToUpdate::setGenre);
		}
		Optional.ofNullable(newName).ifPresent(bookToUpdate::setName);
		Book updatedBook = bookService.updateBook(bookToUpdate);
		return String.format("Book updated: %s", bookConverter.getBookNameWithIdAndGenreAndAuthor(updatedBook));
	}

}
