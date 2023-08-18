package ru.otus.homework.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.homework.rest.BookController;
import ru.otus.homework.rest.dto.AuthorDto;
import ru.otus.homework.rest.dto.BookDto;
import ru.otus.homework.rest.dto.GenreDto;
import ru.otus.homework.service.BookService;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

	private static final String BOOK_NAME_ONEGIN = "Evgeniy Onegin";

	private static final String BOOK_NAME_INSPECTOR = "The Government Inspector";

	private static final Long ID_1 = 1L;

	private static final Long ID_2 = 2L;

	@MockBean
	private BookService bookService;

	@Autowired
	private MockMvc mvc;

	@Test
	void shouldReturnBookList() throws Exception {
		when(bookService.findAllBooks()).thenReturn(getDummyDtoBooks());

		mvc.perform(get("/library/v1/book")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(ID_1))
			.andExpect(jsonPath("$[0].name").value(BOOK_NAME_ONEGIN))
			.andExpect(jsonPath("$[1].id").value(ID_2))
			.andExpect(jsonPath("$[1].name").value(BOOK_NAME_INSPECTOR));

		verify(bookService, times(1)).findAllBooks();
	}

	@Test
	void shouldCreateBookAndReturn200() throws Exception {
		BookDto dummyDtoBook = getDummyDtoBooks().get(0);
		Gson gson = new Gson();
		String bookJson = gson.toJson(dummyDtoBook);

		when(bookService.createBook(dummyDtoBook)).thenReturn(dummyDtoBook);

		mvc.perform(post("/library/v1/book")
				.contentType(MediaType.APPLICATION_JSON)
				.content(bookJson))
			.andExpect(status().isOk());
	}

	@Test
	void shouldUpdateBookAndReturn200() throws Exception {
		BookDto dummyDtoBook = getDummyDtoBooks().get(0);
		Gson gson = new Gson();
		String bookJson = gson.toJson(dummyDtoBook);

		when(bookService.updateBook(dummyDtoBook)).thenReturn(dummyDtoBook);

		mvc.perform(patch("/library/v1/book")
				.contentType(MediaType.APPLICATION_JSON)
				.content(bookJson))
			.andExpect(status().isOk());
	}

	@Test
	void shouldDeleteBookAndReturn200() throws Exception {
		long bookId = 1L;
		doNothing().when(bookService).deleteBookById(bookId);

		mvc.perform(delete("/library/v1/book/{id}", bookId))
			.andExpect(status().isOk());

		verify(bookService, times(1)).deleteBookById(bookId);
	}

	private List<BookDto> getDummyDtoBooks() {
		return List.of(
			new BookDto(ID_1, BOOK_NAME_ONEGIN, getDummyDtoGenres().get(0), getDummyDtoAuthors().get(0)),
			new BookDto(ID_2, BOOK_NAME_INSPECTOR, getDummyDtoGenres().get(1), getDummyDtoAuthors().get(1))
		);
	}

	private List<GenreDto> getDummyDtoGenres() {
		return List.of(
			new GenreDto(ID_1, "Novel"),
			new GenreDto(ID_2, "Play")
		);
	}

	private List<AuthorDto> getDummyDtoAuthors() {
		return List.of(
			new AuthorDto(ID_1, "Aleksandr", "Sergeevich", "Pushkin"),
			new AuthorDto(ID_2, "Nikolay", "Vasilevich", "Gogol")
		);
	}

}
