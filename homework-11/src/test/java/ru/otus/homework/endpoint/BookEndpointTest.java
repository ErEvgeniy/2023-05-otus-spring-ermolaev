package ru.otus.homework.endpoint;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.mapper.BookMapper;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.CommentRepository;
import ru.otus.homework.repository.GenreRepository;
import ru.otus.homework.rest.dto.AuthorDto;
import ru.otus.homework.rest.dto.BookDto;
import ru.otus.homework.rest.dto.GenreDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureWebTestClient
class BookEndpointTest {

	private static final String BOOK_NAME_ONEGIN = "Evgeniy Onegin";

	private static final String BOOK_NAME_INSPECTOR = "The Government Inspector";

	private static final String ID_1 = "1L";

	private static final String ID_2 = "2L";

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private BookRepository bookRepository;

	@MockBean
	private AuthorRepository authorRepository;

	@MockBean
	private GenreRepository genreRepository;

	@MockBean
	private CommentRepository commentRepository;

	@MockBean
	private BookMapper bookMapper;

	@Test
	void shouldFindAllBooks() {
		List<Book> books = getDummyBooks();

		when(bookRepository.findAll()).thenReturn(Flux.fromIterable(books));
		when(bookMapper.toDto(any(Book.class))).thenReturn(new BookDto());

		webTestClient.get()
				.uri("/library/v1/book")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk();

		verify(bookRepository, times(1)).findAll();
		verify(bookMapper, times(1)).toDto(any(Book.class));
	}

	@Test
	void shouldCreateBook() {
		BookDto bookDto = getDummyDtoBooks().get(0);
		Book book = getDummyBooks().get(0);
		ServerRequest request = Mockito.mock(ServerRequest.class);

		when(request.bodyToMono(BookDto.class)).thenReturn(Mono.just(bookDto));
		when(bookMapper.toDomain(any(BookDto.class))).thenReturn(book);
		when(authorRepository.findById(anyString())).thenReturn(Mono.just(getDummyAuthors().get(0)));
		when(genreRepository.findById(anyString())).thenReturn(Mono.just(getDummyGenres().get(0)));
		when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));
		when(bookMapper.toDto(any(Book.class))).thenReturn(bookDto);

		webTestClient.post()
				.uri("/library/v1/book")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(bookDto))
				.exchange()
				.expectStatus().isOk();

		verify(bookMapper).toDomain(any(BookDto.class));
		verify(authorRepository).findById(anyString());
		verify(genreRepository).findById(anyString());
		verify(bookRepository).save(any());
		verify(bookMapper).toDto(any(Book.class));
	}

	@Test
	void shouldUpdateBook() {
		Book existingBook = getDummyBooks().get(0);
		BookDto updatedBookDto = getDummyDtoBooks().get(0);
		updatedBookDto.setName("newName");

		when(bookRepository.findById(existingBook.getId())).thenReturn(Mono.just(existingBook));
		when(authorRepository.findById(anyString())).thenReturn(Mono.just(getDummyAuthors().get(0)));
		when(genreRepository.findById(anyString())).thenReturn(Mono.just(getDummyGenres().get(0)));
		when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(existingBook));
		when(bookMapper.toDto(existingBook)).thenReturn(updatedBookDto);

		webTestClient.patch()
				.uri("/library/v1/book/{id}", existingBook.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(updatedBookDto)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.name").isEqualTo(updatedBookDto.getName());

		verify(bookRepository).findById(existingBook.getId());
		verify(authorRepository).findById(anyString());
		verify(genreRepository).findById(anyString());
		verify(bookRepository).save(existingBook);
		verify(bookMapper).toDto(existingBook);
	}

	@Test
	void shouldDeleteBook() {
		String bookId = "1";

		when(bookRepository.deleteById(bookId)).thenReturn(Mono.empty());
		when(commentRepository.deleteAllByBookId(bookId)).thenReturn(Mono.empty());

		webTestClient.delete()
				.uri("/library/v1/book/{id}", bookId)
				.exchange()
				.expectStatus().isOk();

		verify(bookRepository).deleteById(bookId);
		verify(commentRepository).deleteAllByBookId(bookId);
	}

	private List<BookDto> getDummyDtoBooks() {
		return List.of(
				new BookDto(ID_1, BOOK_NAME_ONEGIN, getDummyDtoGenres().get(0), getDummyDtoAuthors().get(0)),
				new BookDto(ID_2, BOOK_NAME_INSPECTOR, getDummyDtoGenres().get(1), getDummyDtoAuthors().get(1))
		);
	}

	private List<Book> getDummyBooks() {
		return List.of(
				new Book(ID_1, BOOK_NAME_ONEGIN, getDummyGenres().get(0), getDummyAuthors().get(0), null),
				new Book(ID_2, BOOK_NAME_INSPECTOR, getDummyGenres().get(1), getDummyAuthors().get(1), null)
		);
	}

	private List<GenreDto> getDummyDtoGenres() {
		return List.of(
				new GenreDto(ID_1, "Novel"),
				new GenreDto(ID_2, "Play")
		);
	}

	private List<Genre> getDummyGenres() {
		return List.of(
				new Genre(ID_1, "Novel"),
				new Genre(ID_2, "Play")
		);
	}

	private List<AuthorDto> getDummyDtoAuthors() {
		return List.of(
				new AuthorDto(ID_1, "Aleksandr", "Sergeevich", "Pushkin"),
				new AuthorDto(ID_2, "Nikolay", "Vasilevich", "Gogol")
		);
	}

	private List<Author> getDummyAuthors() {
		return List.of(
				new Author(ID_1, "Aleksandr", "Sergeevich", "Pushkin"),
				new Author(ID_2, "Nikolay", "Vasilevich", "Gogol")
		);
	}

}
