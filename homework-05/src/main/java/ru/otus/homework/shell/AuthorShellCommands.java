package ru.otus.homework.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.converter.AuthorConverter;
import ru.otus.homework.domain.Author;
import ru.otus.homework.service.AuthorService;

import java.util.List;
import java.util.Optional;

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
	public String authorById(@ShellOption long authorId) {
		Optional<Author> author = authorService.findAuthorById(authorId);
		if (author.isPresent()) {
			return authorConverter.getAuthorFullNameWithId(author.get());
		} else {
			return String.format("Author with id: %d not found", authorId);
		}
	}

	@ShellMethod(value = "Delete author by id", key = {"ad", "author-delete"})
	public String authorDelete(@ShellOption long authorId) {
		try {
			int deletedRows = authorService.deleteAuthorById(authorId);
			if (deletedRows > 0) {
				return String.format("Author with id: %d successfully deleted", authorId);
			} else {
				return String.format("Author with id: %d was not found to delete", authorId);
			}
		} catch (DataIntegrityViolationException ex) {
			return String.format("Author with id: %d is used. Unable to delete", authorId);
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
		Author createdAuthor = authorService.createAuthor(newAuthor);
		return String.format("Author created: %s", authorConverter.getAuthorFullNameWithId(createdAuthor));
	}

	@ShellMethod(value = "Update existed author", key = {"au", "author-update"})
	public String authorUpdate(
		@ShellOption(value = { "-i", "--id" }) int id,
		@ShellOption(defaultValue = ShellOption.NULL, value = { "-f", "--firstname" }) String newFirstname,
		@ShellOption(defaultValue = ShellOption.NULL, value = { "-p", "--patronymic" }) String newPatronymic,
		@ShellOption(defaultValue = ShellOption.NULL, value = { "-l", "--lastname" }) String newLastname
	) {
		Optional<Author> existedAuthor = authorService.findAuthorById(id);
		if (existedAuthor.isEmpty()) {
			return String.format("Author with id: %d was not found", id);
		}
		Author authorToUpdate = existedAuthor.get();
		if (newFirstname != null && !newFirstname.isEmpty()) {
			authorToUpdate.setFirstname(newFirstname);
		}
		if (newPatronymic != null && !newPatronymic.isEmpty()) {
			authorToUpdate.setPatronymic(newPatronymic);
		}
		if (newLastname != null && !newLastname.isEmpty()) {
			authorToUpdate.setLastname(newLastname);
		}

		Author updatedAuthor = authorService.updateAuthor(authorToUpdate);
		return String.format("Author updated: %s", authorConverter.getAuthorFullNameWithId(updatedAuthor));
	}

}
