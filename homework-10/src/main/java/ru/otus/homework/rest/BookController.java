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
import ru.otus.homework.rest.dto.BookDto;
import ru.otus.homework.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/library/v1")
@RequiredArgsConstructor
public class BookController {

	private final BookService bookService;

	@GetMapping("/book")
	public List<BookDto> bookList() {
		return bookService.findAllBooks();
	}

	@PostMapping("/book")
	public void bookCreate(@Valid @RequestBody BookDto bookDto) {
		bookService.createBook(bookDto);
	}

	@PatchMapping("/book/{id}")
	public void bookUpdate(@PathVariable long id, @Valid @RequestBody BookDto bookDto) {
		bookDto.setId(id);
		bookService.updateBook(bookDto);
	}

	@DeleteMapping("/book/{id}")
	public void bookDelete(@PathVariable long id) {
		bookService.deleteBookById(id);
	}

}
