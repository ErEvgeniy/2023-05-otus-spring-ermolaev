package ru.otus.homework.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.homework.dao.impl.BookDaoJdbc;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(BookDaoJdbc.class)
class BookDaoJdbcTest {

	@Autowired
	private BookDaoJdbc bookDao;

	@Test
	void shouldFindOneBookById() {
		Optional<Book> book = bookDao.findById(1);
		assertThat(book).isPresent();
	}

	@Test
	void shouldNotFindBookById() {
		Optional<Book> book = bookDao.findById(10);
		assertThat(book).isEmpty();
	}

	@Test
	void shouldFindAllBooks() {
		List<Book> books = bookDao.findAll();
		assertThat(books)
			.isNotNull()
			.hasSize(7);
	}

	@Test
	void shouldCreateBook() {
		List<Book> booksBeforeCreate = bookDao.findAll();
		assertThat(booksBeforeCreate)
			.isNotNull()
			.hasSize(7);

		Author author = Author.builder()
			.id(1L)
			.build();

		Genre genre = Genre.builder()
			.id(1L)
			.build();

		Book book = Book.builder()
			.id(8L)
			.name("Test")
			.genre(genre)
			.author(author)
			.build();

		bookDao.create(book);

		List<Book> booksAfterCreate = bookDao.findAll();
		assertThat(booksAfterCreate)
			.isNotNull()
			.hasSize(8);
	}

	@Test
	void shouldUpdateBook() {
		Author author = Author.builder()
			.id(2L)
			.build();

		Genre genre = Genre.builder()
			.id(2L)
			.build();

		Book book = Book.builder()
			.id(1L)
			.name("Test")
			.genre(genre)
			.author(author)
			.build();

		Book updatedBook = bookDao.update(book);
		assertThat(updatedBook.getName()).isEqualTo("Test");
		assertThat(updatedBook.getAuthor().getId()).isEqualTo(2);
		assertThat(updatedBook.getGenre().getId()).isEqualTo(2);
	}

	@Test
	void shouldDeleteBookById() {
		List<Book> booksBeforeCreate = bookDao.findAll();
		assertThat(booksBeforeCreate)
			.isNotNull()
			.hasSize(7);

		bookDao.deleteById(1);

		List<Book> booksAfterCreate = bookDao.findAll();
		assertThat(booksAfterCreate)
			.isNotNull()
			.hasSize(6);
	}

}
