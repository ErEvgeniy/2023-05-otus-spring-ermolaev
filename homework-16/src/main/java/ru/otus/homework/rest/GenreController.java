package ru.otus.homework.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.homework.rest.dto.GenreDto;
import ru.otus.homework.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/library/v1")
@RequiredArgsConstructor
public class GenreController {

	private final GenreService genreService;

	@GetMapping("/genre")
	public List<GenreDto> genreList() {
		return genreService.findAllGenres();
	}

	@PostMapping("/genre")
	public void genreCreate(@Valid @RequestBody GenreDto genreDto) {
		genreService.createGenre(genreDto);
	}

	@PatchMapping("/genre/{id}")
	public void genreUpdate(@PathVariable long id, @Valid @RequestBody GenreDto genreDto) {
		genreDto.setId(id);
		genreService.updateGenre(genreDto);
	}

	@DeleteMapping("/genre/{id}")
	public void genreDelete(@PathVariable long id) {
		genreService.deleteGenreById(id);
	}

}
