package ru.otus.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.dto.AuthorDto;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.dto.GenreDto;
import ru.otus.homework.mapper.AuthorMapper;
import ru.otus.homework.mapper.BookMapper;
import ru.otus.homework.mapper.GenreMapper;
import ru.otus.homework.service.AuthorService;
import ru.otus.homework.service.BookService;
import ru.otus.homework.service.GenreService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
class BookControllerTest {

	@MockBean
	private BookService bookService;

	@MockBean
	private AuthorService authorService;

	@MockBean
	private GenreService genreService;

	@MockBean
	private BookMapper bookMapper;

	@MockBean
	private AuthorMapper authorMapper;

	@MockBean
	private GenreMapper genreMapper;

	@Autowired
	private MockMvc mvc;

	@Test
	void shouldReturnViewWithBookList() throws Exception {
		List<Book> dummyBooks = getDummyBooks();
		List<BookDto> dummyDtoBooks = getDummyDtoBooks();

		when(bookService.findAllBooks()).thenReturn(dummyBooks);
		when(bookMapper.toDtoList(dummyBooks)).thenReturn(dummyDtoBooks);

		mvc.perform(get("/book/list"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("books"))
			.andExpect(model().attribute("books", dummyDtoBooks))
			.andExpect(view().name("book/list"));

		verify(bookService, times(1)).findAllBooks();
		verify(bookMapper, times(1)).toDtoList(dummyBooks);
	}

	@Test
	void shouldReturnViewForBookEdit() throws Exception {
		Book dummyBook = getDummyBooks().get(0);
		BookDto dummyDtoBook = getDummyDtoBooks().get(0);
		List<Genre> dummyGenres = getDummyGenres();
		List<GenreDto> dummyDtoGenres = getDummyDtoGenres();
		List<Author> dummyAuthors = getDummyAuthors();
		List<AuthorDto> dummyDtoAuthors = getDummyDtoAuthors();

		when(bookService.findBookById(anyLong())).thenReturn(dummyBook);
		when(bookMapper.toDto(dummyBook)).thenReturn(dummyDtoBook);
		when(authorService.findAllAuthors()).thenReturn(dummyAuthors);
		when(authorMapper.toDtoList(dummyAuthors)).thenReturn(dummyDtoAuthors);
		when(genreService.findAllGenres()).thenReturn(dummyGenres);
		when(genreMapper.toDtoList(dummyGenres)).thenReturn(dummyDtoGenres);

		mvc.perform(get("/book").param("id", "1"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("book"))
			.andExpect(model().attribute("book", dummyDtoBook))
			.andExpect(model().attributeExists("authors"))
			.andExpect(model().attribute("authors", dummyDtoAuthors))
			.andExpect(model().attributeExists("genres"))
			.andExpect(model().attribute("genres", dummyDtoGenres))
			.andExpect(view().name("book/edit"));

		verify(bookService, times(1)).findBookById(anyLong());
		verify(bookMapper, times(1)).toDto(dummyBook);
		verify(authorService, times(1)).findAllAuthors();
		verify(authorMapper, times(1)).toDtoList(dummyAuthors);
		verify(genreService, times(1)).findAllGenres();
		verify(genreMapper, times(1)).toDtoList(dummyGenres);
	}

	@Test
	void shouldReturnViewForBookCreate() throws Exception {
		List<Genre> dummyGenres = getDummyGenres();
		List<GenreDto> dummyDtoGenres = getDummyDtoGenres();
		List<Author> dummyAuthors = getDummyAuthors();
		List<AuthorDto> dummyDtoAuthors = getDummyDtoAuthors();

		when(authorService.findAllAuthors()).thenReturn(dummyAuthors);
		when(authorMapper.toDtoList(dummyAuthors)).thenReturn(dummyDtoAuthors);
		when(genreService.findAllGenres()).thenReturn(dummyGenres);
		when(genreMapper.toDtoList(dummyGenres)).thenReturn(dummyDtoGenres);

		mvc.perform(get("/book/create"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("book"))
			.andExpect(model().attributeExists("authors"))
			.andExpect(model().attribute("authors", dummyDtoAuthors))
			.andExpect(model().attributeExists("genres"))
			.andExpect(model().attribute("genres", dummyDtoGenres))
			.andExpect(view().name("book/create"));

		verify(authorService, times(1)).findAllAuthors();
		verify(authorMapper, times(1)).toDtoList(dummyAuthors);
		verify(genreService, times(1)).findAllGenres();
		verify(genreMapper, times(1)).toDtoList(dummyGenres);
	}

	@Test
	void shouldDeleteBookAndRedirectToBookList() throws Exception {
		long bookId = 1L;
		doNothing().when(bookService).deleteBookById(bookId);

		mvc.perform(post("/book/{id}", bookId))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/book/list"));

		verify(bookService, times(1)).deleteBookById(bookId);
	}

	@Test
	void shouldCreateBookAndRedirectToBookList() throws Exception {
		Book dummyBook = getDummyBooks().get(0);
		BookDto dummyDtoBook = getDummyDtoBooks().get(0);
		when(bookMapper.toDomain(dummyDtoBook)).thenReturn(dummyBook);
		when(bookService.createBook(dummyBook)).thenReturn(dummyBook);

		mvc.perform(post("/book").flashAttr("bookDto", dummyDtoBook))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/book/list"));

		verify(bookMapper, times(1)).toDomain(dummyDtoBook);
		verify(bookService, times(1)).createBook(dummyBook);
	}

	@Test
	void shouldUpdateBookAndRedirectToBookList() throws Exception {
		Book dummyBook = getDummyBooks().get(0);
		BookDto dummyDtoBook = getDummyDtoBooks().get(0);
		when(bookMapper.toDomain(dummyDtoBook)).thenReturn(dummyBook);
		when(bookService.updateBook(dummyBook)).thenReturn(dummyBook);

		mvc.perform(post("/book")
				.param("id", "1")
				.flashAttr("bookDto", dummyDtoBook))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/book/list"));

		verify(bookMapper, times(1)).toDomain(dummyDtoBook);
		verify(bookService, times(1)).updateBook(dummyBook);
	}

	private List<Book> getDummyBooks() {
		return List.of(
			new Book(1L, "Evgeniy Onegin", getDummyGenres().get(0), getDummyAuthors().get(0), null),
			new Book(2L, "The Government Inspector", getDummyGenres().get(1), getDummyAuthors().get(1), null)
		);
	}

	private List<BookDto> getDummyDtoBooks() {
		return List.of(
			new BookDto(1L, "Evgeniy Onegin", getDummyDtoGenres().get(0), getDummyDtoAuthors().get(0)),
			new BookDto(2L, "The Government Inspector", getDummyDtoGenres().get(1), getDummyDtoAuthors().get(1))
		);
	}

	private List<Genre> getDummyGenres() {
		return List.of(
			new Genre(1L, "Novel"),
			new Genre(2L, "Play")
		);
	}

	private List<GenreDto> getDummyDtoGenres() {
		return List.of(
			new GenreDto(1L, "Novel"),
			new GenreDto(2L, "Play")
		);
	}

	private List<Author> getDummyAuthors() {
		return List.of(
			new Author(1L, "Aleksandr", "Sergeevich", "Pushkin"),
			new Author(2L, "Nikolay", "Vasilevich", "Gogol")
		);
	}

	private List<AuthorDto> getDummyDtoAuthors() {
		return List.of(
			new AuthorDto(1L, "Aleksandr", "Sergeevich", "Pushkin"),
			new AuthorDto(2L, "Nikolay", "Vasilevich", "Gogol")
		);
	}

}
