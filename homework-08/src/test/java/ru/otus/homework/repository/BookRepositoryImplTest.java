package ru.otus.homework.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class BookRepositoryImplTest {

	private static final String BOOK_NAME = "Test";

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private GenreRepository genreRepository;

	@Test
	void shouldFindOneBookById() {
		List<Book> allBooks = bookRepository.findAll();
		Book expect = allBooks.get(0);

		Optional<Book> actual = bookRepository.findById(expect.getId());

		assertThat(actual).isPresent();
		assertThat(actual.get().getName()).isEqualTo(expect.getName());
		assertThat(actual.get().getAuthor().getFirstname()).isNotNull();
		assertThat(actual.get().getGenre().getName()).isNotNull();
		assertThat(actual.get().getComments())
			.isNotNull()
			.isNotEmpty();
	}

	@Test
	void shouldNotFindBookById() {
		Optional<Book> book = bookRepository.findById("nonExistId");
		assertThat(book).isEmpty();
	}

	@Test
	void shouldFindAllBooks() {
		List<Book> books = bookRepository.findAll();
		assertThat(books)
			.isNotNull()
			.isNotEmpty();
	}

	@Test
	void shouldCreateBook() {
		Author author = authorRepository.findAll().get(0);
		Genre genre = genreRepository.findAll().get(0);

		Book book = Book.builder()
			.name(BOOK_NAME)
			.genre(genre)
			.author(author)
			.build();

		Book createdBook = bookRepository.save(book);

		assertThat(createdBook).isNotNull();
		assertThat(createdBook.getId()).isNotNull();
		assertThat(createdBook.getName()).isEqualTo(BOOK_NAME);
		assertThat(createdBook.getAuthor()).isEqualTo(author);
		assertThat(createdBook.getGenre()).isEqualTo(genre);
	}

	@Test
	void shouldUpdateBook() {
		Author author = authorRepository.findAll().get(0);
		Genre genre = genreRepository.findAll().get(0);

		Book book = Book.builder()
			.name(BOOK_NAME)
			.genre(genre)
			.author(author)
			.build();

		Book updatedBook = bookRepository.save(book);
		assertThat(updatedBook.getName()).isEqualTo(BOOK_NAME);
		assertThat(updatedBook.getAuthor()).isNotNull();
		assertThat(updatedBook.getGenre().getId()).isNotNull();
	}

	@Test
	void shouldDeleteBookById() {
		Book book = bookRepository.findAll().get(0);

		Optional<Book> beforeDelete = bookRepository.findById(book.getId());
		bookRepository.delete(book);
		Optional<Book> afterDelete = bookRepository.findById(book.getId());

		assertThat(beforeDelete).isNotEmpty();
		assertThat(afterDelete).isEmpty();
	}

	@Test
	void shouldExistBookByAuthorId() {
		Author author = authorRepository.findAll().get(0);

		boolean result = bookRepository.existsBookByAuthorId(author.getId());

		assertThat(result).isTrue();
	}

	@Test
	void shouldExistBookByGenreId() {
		Genre genre = genreRepository.findAll().get(0);

		boolean result = bookRepository.existsBookByGenreId(genre.getId());

		assertThat(result).isTrue();
	}

}
