package ru.otus.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.homework.dto.AuthorDto;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.dto.GenreDto;
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

	@Autowired
	private MockMvc mvc;

	@Test
	void shouldReturnViewWithBookList() throws Exception {
		List<BookDto> dummyDtoBooks = getDummyDtoBooks();

		when(bookService.findAllBooks()).thenReturn(dummyDtoBooks);

		mvc.perform(get("/book/list"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("books"))
			.andExpect(model().attribute("books", dummyDtoBooks))
			.andExpect(view().name("book/list"));

		verify(bookService, times(1)).findAllBooks();
	}

	@Test
	void shouldReturnViewForBookEdit() throws Exception {
		BookDto dummyDtoBook = getDummyDtoBooks().get(0);
		List<GenreDto> dummyDtoGenres = getDummyDtoGenres();
		List<AuthorDto> dummyDtoAuthors = getDummyDtoAuthors();

		when(bookService.findBookById(anyLong())).thenReturn(dummyDtoBook);
		when(authorService.findAllAuthors()).thenReturn(dummyDtoAuthors);
		when(genreService.findAllGenres()).thenReturn(dummyDtoGenres);

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
		verify(authorService, times(1)).findAllAuthors();
		verify(genreService, times(1)).findAllGenres();
	}

	@Test
	void shouldReturnViewForBookCreate() throws Exception {
		List<GenreDto> dummyDtoGenres = getDummyDtoGenres();
		List<AuthorDto> dummyDtoAuthors = getDummyDtoAuthors();

		when(authorService.findAllAuthors()).thenReturn(dummyDtoAuthors);
		when(genreService.findAllGenres()).thenReturn(dummyDtoGenres);

		mvc.perform(get("/book/create"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("book"))
			.andExpect(model().attributeExists("authors"))
			.andExpect(model().attribute("authors", dummyDtoAuthors))
			.andExpect(model().attributeExists("genres"))
			.andExpect(model().attribute("genres", dummyDtoGenres))
			.andExpect(view().name("book/create"));

		verify(authorService, times(1)).findAllAuthors();
		verify(genreService, times(1)).findAllGenres();
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
		BookDto dummyDtoBook = getDummyDtoBooks().get(0);
		when(bookService.createBook(dummyDtoBook)).thenReturn(dummyDtoBook);

		mvc.perform(post("/book").flashAttr("bookDto", dummyDtoBook))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/book/list"));

		verify(bookService, times(1)).createBook(dummyDtoBook);
	}

	@Test
	void shouldUpdateBookAndRedirectToBookList() throws Exception {
		BookDto dummyDtoBook = getDummyDtoBooks().get(0);
		when(bookService.updateBook(dummyDtoBook)).thenReturn(dummyDtoBook);

		mvc.perform(post("/book")
				.param("id", "1")
				.flashAttr("bookDto", dummyDtoBook))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/book/list"));

		verify(bookService, times(1)).updateBook(dummyDtoBook);
	}

	private List<BookDto> getDummyDtoBooks() {
		return List.of(
			new BookDto(1L, "Evgeniy Onegin", getDummyDtoGenres().get(0), getDummyDtoAuthors().get(0)),
			new BookDto(2L, "The Government Inspector", getDummyDtoGenres().get(1), getDummyDtoAuthors().get(1))
		);
	}

	private List<GenreDto> getDummyDtoGenres() {
		return List.of(
			new GenreDto(1L, "Novel"),
			new GenreDto(2L, "Play")
		);
	}

	private List<AuthorDto> getDummyDtoAuthors() {
		return List.of(
			new AuthorDto(1L, "Aleksandr", "Sergeevich", "Pushkin"),
			new AuthorDto(2L, "Nikolay", "Vasilevich", "Gogol")
		);
	}

}
