package ru.otus.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.homework.dto.AuthorDto;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.dto.GenreDto;
import ru.otus.homework.service.AuthorService;
import ru.otus.homework.service.BookService;
import ru.otus.homework.service.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

	private final BookService bookService;

	private final AuthorService authorService;

	private final GenreService genreService;

	@GetMapping("/book/list")
	public String bookList(Model model) {
		List<BookDto> bookDtoList = bookService.findAllBooks();
		model.addAttribute("books", bookDtoList);
		return "book/list";
	}

	@GetMapping("/book")
	public String bookEdit(@RequestParam long id, Model model) {
		BookDto bookDto = bookService.findBookById(id);
		model.addAttribute("book", bookDto);

		List<AuthorDto> authorDtoList = authorService.findAllAuthors();
		model.addAttribute("authors", authorDtoList);

		List<GenreDto> genreDtoList = genreService.findAllGenres();
		model.addAttribute("genres", genreDtoList);

		return "book/edit";
	}

	@GetMapping("/book/create")
	public String bookCreate(Model model) {
		BookDto bookDto = new BookDto();
		model.addAttribute("book", bookDto);

		List<AuthorDto> authorDtoList = authorService.findAllAuthors();
		model.addAttribute("authors", authorDtoList);

		List<GenreDto> genreDtoList = genreService.findAllGenres();
		model.addAttribute("genres", genreDtoList);

		return "book/create";
	}

	@PostMapping("/book/{id}")
	public String bookDelete(@PathVariable long id) {
		bookService.deleteBookById(id);
		return "redirect:/book/list";
	}

	@PostMapping("/book")
	public String bookCreate(@ModelAttribute BookDto bookDto) {
		bookService.createBook(bookDto);
		return "redirect:/book/list";
	}

	@PostMapping(value = "/book", params = {"id"})
	public String bookUpdate(@RequestParam long id, @ModelAttribute BookDto bookDto) {
		bookDto.setId(id);
		bookService.updateBook(bookDto);
		return "redirect:/book/list";
	}

}
