package ru.otus.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.dto.AuthorDto;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.dto.GenreDto;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.mapper.AuthorMapper;
import ru.otus.homework.mapper.BookMapper;
import ru.otus.homework.mapper.GenreMapper;
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

	private final BookMapper bookMapper;

	private final AuthorMapper authorMapper;

	private final GenreMapper genreMapper;

	@GetMapping("/book/list")
	public String bookList(Model model) {
		List<Book> books = bookService.findAllBooks();
		List<BookDto> bookDtoList = bookMapper.toDtoList(books);
		model.addAttribute("books", bookDtoList);
		return "book/list";
	}

	@GetMapping("/book")
	public String bookEdit(@RequestParam long id, Model model) {
		Book book = bookService.findBookById(id);
		if (book == null) {
			throw new DataNotFoundException();
		}
		BookDto bookDto = bookMapper.toDto(book);
		model.addAttribute("book", bookDto);

		List<Author> authors = authorService.findAllAuthors();
		List<AuthorDto> authorDtoList = authorMapper.toDtoList(authors);
		model.addAttribute("authors", authorDtoList);

		List<Genre> genres = genreService.findAllGenres();
		List<GenreDto> genreDtoList = genreMapper.toDtoList(genres);
		model.addAttribute("genres", genreDtoList);

		return "book/edit";
	}

	@GetMapping("/book/create")
	public String bookCreate(Model model) {
		BookDto bookDto = new BookDto();
		model.addAttribute("book", bookDto);

		List<Author> authors = authorService.findAllAuthors();
		List<AuthorDto> authorDtoList = authorMapper.toDtoList(authors);
		model.addAttribute("authors", authorDtoList);

		List<Genre> genres = genreService.findAllGenres();
		List<GenreDto> genreDtoList = genreMapper.toDtoList(genres);
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
		Book book = bookMapper.toDomain(bookDto);
		bookService.createBook(book);
		return "redirect:/book/list";
	}

	@PostMapping(value = "/book", params = {"id"})
	public String bookUpdate(@RequestParam long id, @ModelAttribute BookDto bookDto) {
		Book book = bookMapper.toDomain(bookDto);
		book.setId(id);
		bookService.updateBook(book);
		return "redirect:/book/list";
	}

}
