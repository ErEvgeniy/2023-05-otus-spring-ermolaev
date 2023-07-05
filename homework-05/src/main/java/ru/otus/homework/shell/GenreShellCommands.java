package ru.otus.homework.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.converter.GenreConverter;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.service.GenreService;

import java.util.List;
import java.util.Optional;

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
	public String genreById(@ShellOption long genreId) {
		Optional<Genre> genre = genreService.findGenreById(genreId);
		if (genre.isPresent()) {
			return genreConverter.getGenreWithId(genre.get());
		} else {
			return String.format("Genre with id: %d not found", genreId);
		}
	}

	@ShellMethod(value = "Delete genre by id", key = {"gd", "genre-delete"})
	public String genreDelete(@ShellOption long genreId) {
		try {
			int deletedRows = genreService.deleteGenreById(genreId);
			if (deletedRows > 0) {
				return String.format("Genre with id: %d successfully deleted", genreId);
			} else {
				return String.format("Genre with id: %d was not found to delete", genreId);
			}
		} catch (DataIntegrityViolationException ex) {
			return String.format("Genre with id: %d is used. Unable to delete", genreId);
		}
	}

	@ShellMethod(value = "Create new genre", key = {"gc", "genre-create"})
	public String genreCreate(
		@ShellOption(value = { "-n", "--name" }) String name
	) {
		Genre genre = Genre.builder()
			.name(name)
			.build();
		Genre createdGenre = genreService.createGenre(genre);
		return String.format("Genre created: %s", genreConverter.getGenreWithId(createdGenre));
	}

	@ShellMethod(value = "Update existed genre", key = {"gu", "genre-update"})
	public String genreUpdate(
		@ShellOption(value = { "-i", "--id" }) int id,
		@ShellOption(defaultValue = ShellOption.NULL, value = { "-n", "--name" }) String newName
	) {
		Optional<Genre> existedGenre = genreService.findGenreById(id);
		if (existedGenre.isEmpty()) {
			return String.format("Genre with id: %d was not found", id);
		}
		Genre genreToUpdate = existedGenre.get();

		if (newName != null && !newName.isEmpty()) {
			genreToUpdate.setName(newName);
		}

		Genre updatedGenre = genreService.updateGenre(genreToUpdate);
		return String.format("Genre updated: %s", genreConverter.getGenreWithId(updatedGenre));
	}

}
