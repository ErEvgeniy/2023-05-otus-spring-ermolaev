package ru.otus.homework.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.homework.dao.BookDao;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.service.impl.BookServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookServiceImplTest {

	private static final String BOOK_NAME = "Test";

	private static final Long BOOK_ID = 1L;

	private static final Long AUTHOR_ID = 2L;

	private static final Long GENRE_ID = 3L;

	@Mock
	private BookDao bookDao;

	@Mock
	private AuthorService authorService;

	@Mock
	private GenreService genreService;

	@InjectMocks
	private BookServiceImpl bookService;

	@Test
	void shouldFindOneBookById() {
		when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(getDummyBook()));
		Book book = bookService.findBookById(BOOK_ID);

		assertThat(book).isNotNull();
		assertThat(book.getId()).isEqualTo(BOOK_ID);
		assertThat(book.getName()).isEqualTo(BOOK_NAME);
		assertThat(book.getAuthor().getId()).isEqualTo(AUTHOR_ID);
		assertThat(book.getGenre().getId()).isEqualTo(GENRE_ID);

		verify(bookDao, times(1)).findById(BOOK_ID);
	}

	@Test
	void shouldNotFindBookById() {
		when(bookDao.findById(BOOK_ID)).thenReturn(Optional.empty());

		assertThrows(DataNotFoundException.class, () -> bookService.findBookById(BOOK_ID));

		verify(bookDao, times(1)).findById(BOOK_ID);
	}

	@Test
	void shouldFindAllBooks() {
		when(bookDao.findAll()).thenReturn(List.of(getDummyBook()));
		List<Book> book = bookService.findAllBooks();

		assertThat(book)
			.isNotNull()
			.hasSize(1);

		verify(bookDao, times(1)).findAll();
	}

	@Test
	void shouldCreateBook() {
		Book bookToCreate = getDummyBook();
		when(bookDao.create(bookToCreate)).thenReturn(bookToCreate);
		when(authorService.findAuthorById(AUTHOR_ID)).thenReturn(getDummyAuthor());
		when(genreService.findGenreById(GENRE_ID)).thenReturn(getDummyGenre());
		Book book = bookService.createBook(bookToCreate);

		assertThat(book)
			.isNotNull()
			.isEqualTo(bookToCreate);

		verify(authorService, times(1)).findAuthorById(AUTHOR_ID);
		verify(genreService, times(1)).findGenreById(GENRE_ID);
		verify(bookDao, times(1)).create(bookToCreate);
	}

	@Test
	void shouldUpdateBook() {
		Book bookToUpdate = getDummyBook();
		when(bookDao.update(bookToUpdate)).thenReturn(bookToUpdate);
		when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(bookToUpdate));
		when(authorService.findAuthorById(AUTHOR_ID)).thenReturn(getDummyAuthor());
		when(genreService.findGenreById(GENRE_ID)).thenReturn(getDummyGenre());
		Book book = bookService.updateBook(bookToUpdate);

		assertThat(book)
			.isNotNull()
			.isEqualTo(bookToUpdate);

		verify(bookDao, times(1)).findById(BOOK_ID);
		verify(authorService, times(1)).findAuthorById(AUTHOR_ID);
		verify(genreService, times(1)).findGenreById(GENRE_ID);
		verify(bookDao, times(1)).update(bookToUpdate);
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
