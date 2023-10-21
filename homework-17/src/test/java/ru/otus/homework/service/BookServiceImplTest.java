package ru.otus.homework.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.GenreRepository;
import ru.otus.homework.service.impl.BookServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataMongoTest
class BookServiceImplTest {

	private static final String BOOK_NAME = "Test";

	private static final String BOOK_ID = "BOOK_ID";

	private static final String AUTHOR_ID = "AUTHOR_ID";

	private static final String GENRE_ID = "GENRE_ID";

	@Mock
	private BookRepository bookRepository;

	@Mock
	private AuthorRepository authorRepository;

	@Mock
	private GenreRepository genreRepository;

	@InjectMocks
	private BookServiceImpl bookService;

	@Test
	void shouldFindOneBookById() {
		when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(getDummyBook()));
		Book book = bookService.findBookById(BOOK_ID);

		assertThat(book).isNotNull();
		assertThat(book.getId()).isEqualTo(BOOK_ID);
		assertThat(book.getName()).isEqualTo(BOOK_NAME);
		assertThat(book.getAuthor().getId()).isEqualTo(AUTHOR_ID);
		assertThat(book.getGenre().getId()).isEqualTo(GENRE_ID);

		verify(bookRepository, times(1)).findById(BOOK_ID);
	}

	@Test
	void shouldNotFindBookById() {
		when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.empty());

		assertThrows(DataNotFoundException.class, () -> bookService.findBookById(BOOK_ID));

		verify(bookRepository, times(1)).findById(BOOK_ID);
	}

	@Test
	void shouldFindAllBooks() {
		when(bookRepository.findAll()).thenReturn(List.of(getDummyBook()));
		List<Book> book = bookService.findAllBooks();

		assertThat(book)
			.isNotNull()
			.hasSize(1);

		verify(bookRepository, times(1)).findAll();
	}

	@Test
	void shouldCreateBook() {
		Book bookToCreate = getDummyBook();
		when(bookRepository.save(bookToCreate)).thenReturn(bookToCreate);
		when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(getDummyAuthor()));
		when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(getDummyGenre()));
		Book book = bookService.createBook(bookToCreate);

		assertThat(book)
			.isNotNull()
			.isEqualTo(bookToCreate);

		verify(authorRepository, times(1)).findById(AUTHOR_ID);
		verify(genreRepository, times(1)).findById(GENRE_ID);
		verify(bookRepository, times(1)).save(bookToCreate);
	}

	@Test
	void shouldUpdateBook() {
		Book bookToUpdate = getDummyBook();
		when(bookRepository.save(bookToUpdate)).thenReturn(bookToUpdate);
		when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(bookToUpdate));
		when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(getDummyAuthor()));
		when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(getDummyGenre()));
		Book book = bookService.updateBook(bookToUpdate);

		assertThat(book)
			.isNotNull()
			.isEqualTo(bookToUpdate);

		verify(bookRepository, times(1)).findById(BOOK_ID);
		verify(authorRepository, times(1)).findById(AUTHOR_ID);
		verify(genreRepository, times(1)).findById(GENRE_ID);
		verify(bookRepository, times(1)).save(bookToUpdate);
	}

	private Book getDummyBook() {
		Author author = getDummyAuthor();
		Genre genre = getDummyGenre();

		return Book.builder()
			.id(BOOK_ID)
			.name(BOOK_NAME)
			.genre(genre)
			.author(author)
			.build();
	}

	private Author getDummyAuthor() {
		return Author.builder()
			.id(AUTHOR_ID)
			.build();
	}

	private Genre getDummyGenre() {
		return Genre.builder()
			.id(GENRE_ID)
			.build();
	}
}
