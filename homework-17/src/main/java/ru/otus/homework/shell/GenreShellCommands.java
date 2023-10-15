package ru.otus.homework.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.converter.GenreConverter;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.service.GenreService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class GenreShellCommands {

	private final GenreService genreService;

	private final GenreConverter genreConverter;

	@ShellMethod(value = "Print genre list", key = {"gl", "genre-list"})
	public String genreList() {
		List<Genre> genres = genreService.findAllGenres();
		StringBuilder builder = new StringBuilder("Existed genres:").append(System.lineSeparator());
		for (Genre genre : genres) {
			builder.append(genreConverter.getGenreWithId(genre)).append(System.lineSeparator());
		}
		return builder.toString();
	}

	@ShellMethod(value = "Print genre by id", key = {"g", "genre-by-id"})
	public String genreById(@ShellOption String genreId) {
		try {
			Genre genre = genreService.findGenreById(genreId);
			return genreConverter.getGenreWithId(genre);
		} catch (DataNotFoundException ex) {
			return ex.getMessage();
		}
	}

	@ShellMethod(value = "Delete genre by id", key = {"gd", "genre-delete"})
	public String genreDelete(@ShellOption String genreId) {
		try {
			genreService.deleteGenreById(genreId);
			return String.format("Genre with id: %s successfully deleted", genreId);
		} catch (DataIntegrityViolationException ex) {
			return String.format("Genre with id: %s is used. Unable to delete", genreId);
		}
	}

	@ShellMethod(value = "Create new genre", key = {"gc", "genre-create"})
	public String genreCreate(
		@ShellOption(value = { "-n", "--name" }) String name
	) {
		Genre genre = Genre.builder()
			.name(name)
			.build();

		Genre createdGenre;
		try {
			createdGenre = genreService.createGenre(genre);
		} catch (DataNotFoundException ex) {
			return ex.getMessage();
		}
		return String.format("Genre created: %s", genreConverter.getGenreWithId(createdGenre));
	}

	@ShellMethod(value = "Update existed genre", key = {"gu", "genre-update"})
	public String genreUpdate(
		@ShellOption(value = { "-i", "--id" }) String id,
		@ShellOption(defaultValue = ShellOption.NULL, value = { "-n", "--name" }) String newName
	) {
		Genre genre = Genre.builder()
			.id(id)
			.name(newName)
			.build();

		Genre updatedGenre;
		try {
			updatedGenre = genreService.updateGenre(genre);
		} catch (DataNotFoundException ex) {
			return ex.getMessage();
		}
		return String.format("Genre updated: %s", genreConverter.getGenreWithId(updatedGenre));
	}

}
