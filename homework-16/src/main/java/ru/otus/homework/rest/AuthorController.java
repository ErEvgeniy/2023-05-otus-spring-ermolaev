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
import ru.otus.homework.rest.dto.AuthorDto;
import ru.otus.homework.service.AuthorService;

import java.util.List;

@RestController
@RequestMapping("/library/v1")
@RequiredArgsConstructor
public class AuthorController {

	private final AuthorService authorService;

	@GetMapping("/author")
	public List<AuthorDto> authorList() {
		return authorService.findAllAuthors();
	}

	@PostMapping("/author")
	public void authorCreate(@Valid @RequestBody AuthorDto authorDto) {
		authorService.createAuthor(authorDto);
	}

	@PatchMapping("/author/{id}")
	public void authorUpdate(@PathVariable long id, @Valid @RequestBody AuthorDto authorDto) {
		authorDto.setId(id);
		authorService.updateAuthor(authorDto);
	}

	@DeleteMapping("/author/{id}")
	public void authorDelete(@PathVariable long id) {
		authorService.deleteAuthorById(id);
	}

}
