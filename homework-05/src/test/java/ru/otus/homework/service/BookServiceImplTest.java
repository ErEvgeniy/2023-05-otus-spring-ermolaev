package ru.otus.homework.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.homework.dao.BookDao;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.service.impl.BookServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookServiceImplTest {

	private static final String BOOK_NAME = "Test";

	private static final int BOOK_ID = 1;

	private static final int AUTHOR_ID = 2;

	private static final int GENRE_ID = 3;

	@Mock
	private BookDao bookDao;

	@InjectMocks
	private BookServiceImpl bookService;

	@Test
	void shouldFindOneBookById() {
		when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(getDummyBook()));
		Optional<Book> book = bookService.findBookById(BOOK_ID);

		assertThat(book).isPresent();
		assertThat(book.get().getId()).isEqualTo(BOOK_ID);
		assertThat(book.get().getName()).isEqualTo(BOOK_NAME);
		assertThat(book.get().getAuthor().getId()).isEqualTo(AUTHOR_ID);
		assertThat(book.get().getGenre().getId()).isEqualTo(GENRE_ID);

		verify(bookDao, times(1)).findById(BOOK_ID);
	}

	@Test
	void shouldNotFindBookById() {
		when(bookDao.findById(BOOK_ID)).thenReturn(Optional.empty());
		Optional<Book> book = bookService.findBookById(BOOK_ID);

		assertThat(book).isEmpty();

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
		Book book = bookService.createBook(bookToCreate);

		assertThat(book)
			.isNotNull()
			.isEqualTo(bookToCreate);

		verify(bookDao, times(1)).create(bookToCreate);
	}

	@Test
	void shouldUpdateBook() {
		Book bookToUpdate = getDummyBook();
		when(bookDao.update(bookToUpdate)).thenReturn(bookToUpdate);
		Book book = bookService.updateBook(bookToUpdate);

		assertThat(book)
			.isNotNull()
			.isEqualTo(bookToUpdate);

		verify(bookDao, times(1)).update(bookToUpdate);
	}

	@Test
	void shouldDeleteBookById() {
		when(bookDao.deleteById(BOOK_ID)).thenReturn(1);
		int processedRows = bookService.deleteBookById(BOOK_ID);

		assertThat(processedRows)
			.isEqualTo(1);

		verify(bookDao, times(1)).deleteById(BOOK_ID);
	}

	private Book getDummyBook() {
		Author author = Author.builder()
			.id(AUTHOR_ID)
			.build();

		Genre genre = Genre.builder()
			.id(GENRE_ID)
			.build();

		return Book.builder()
			.id(BOOK_ID)
			.name(BOOK_NAME)
			.genre(genre)
			.author(author)
			.build();
	}
}
