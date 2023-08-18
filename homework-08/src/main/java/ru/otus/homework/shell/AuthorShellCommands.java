package ru.otus.homework.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.converter.AuthorConverter;
import ru.otus.homework.domain.Author;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.service.AuthorService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class AuthorShellCommands extends AbstractShellComponent {

	private final AuthorService authorService;

	private final AuthorConverter authorConverter;

	@ShellMethod(value = "Print author list", key = {"al", "author-list"})
	public String authorList() {
		List<Author> authors = authorService.findAllAuthors();
		StringBuilder builder = new StringBuilder("Existed authors:").append(System.lineSeparator());
		for (Author author : authors) {
			builder.append(authorConverter.getAuthorFullNameWithId(author)).append(System.lineSeparator());
		}
		return builder.toString();
	}

	@ShellMethod(value = "Print author by id", key = {"a", "author-by-id"})
	public String authorById(@ShellOption String authorId) {
		try {
			Author author = authorService.findAuthorById(authorId);
			return authorConverter.getAuthorFullNameWithId(author);
		} catch (DataNotFoundException ex) {
			return ex.getMessage();
		}
	}

	@ShellMethod(value = "Delete author by id", key = {"ad", "author-delete"})
	public String authorDelete(@ShellOption String authorId) {
		try {
			authorService.deleteAuthorById(authorId);
			return String.format("Author with id: %s successfully deleted", authorId);
		} catch (DataIntegrityViolationException ex) {
			return String.format("Author with id: %s is used. Unable to delete", authorId);
		}
	}

	@ShellMethod(value = "Create new author", key = {"ac", "author-create"})
	public String authorCreate(
		@ShellOption(value = { "-f", "--firstname" }) String firstname,
		@ShellOption(value = { "-p", "--patronymic" }) String patronymic,
		@ShellOption(value = { "-l", "--lastname" }) String lastname
	) {
		Author newAuthor = Author.builder()
			.firstname(firstname)
			.patronymic(patronymic)
			.lastname(lastname)
			.build();

		Author createdAuthor;
		try {
			createdAuthor = authorService.createAuthor(newAuthor);
		} catch (DataNotFoundException ex) {
			return ex.getMessage();
		}
		return String.format("Author created: %s", authorConverter.getAuthorFullNameWithId(createdAuthor));
	}

	@ShellMethod(value = "Update existed author", key = {"au", "author-update"})
	public String authorUpdate(
		@ShellOption(value = { "-i", "--id" }) String id,
		@ShellOption(defaultValue = ShellOption.NULL, value = { "-f", "--firstname" }) String newFirstname,
		@ShellOption(defaultValue = ShellOption.NULL, value = { "-p", "--patronymic" }) String newPatronymic,
		@ShellOption(defaultValue = ShellOption.NULL, value = { "-l", "--lastname" }) String newLastname
	) {
		Author author = Author.builder()
			.id(id)
			.firstname(newFirstname)
			.patronymic(newPatronymic)
			.lastname(newLastname)
			.build();

		Author updatedAuthor;
		try {
			updatedAuthor = authorService.updateAuthor(author);
		} catch (DataNotFoundException ex) {
			return ex.getMessage();
		}
		return String.format("Author updated: %s", authorConverter.getAuthorFullNameWithId(updatedAuthor));
	}

}
