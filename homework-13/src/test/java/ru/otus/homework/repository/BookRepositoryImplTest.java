package ru.otus.homework.repository;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryImplTest {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void shouldFindOneBookById() {
		SessionFactory sessionFactory = entityManager.getEntityManager()
			.getEntityManagerFactory()
			.unwrap(SessionFactory.class);
		sessionFactory.getStatistics().setStatisticsEnabled(true);
		sessionFactory.getStatistics().clear();

		Optional<Book> actual = bookRepository.findById(1L);
		Book expect = entityManager.find(Book.class, 1L);

		assertThat(actual).isPresent();
		assertThat(actual.get().getName()).isEqualTo(expect.getName());
		assertThat(actual.get().getAuthor().getFirstname()).isNotNull();
		assertThat(actual.get().getGenre().getName()).isNotNull();
		assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
	}

	@Test()
	void shouldNotFindBookById() {
		Optional<Book> book = bookRepository.findById(10L);
		assertThat(book).isEmpty();
	}

	@Test
	void shouldFindAllBooks() {
		SessionFactory sessionFactory = entityManager.getEntityManager()
			.getEntityManagerFactory()
			.unwrap(SessionFactory.class);
		sessionFactory.getStatistics().setStatisticsEnabled(true);
		sessionFactory.getStatistics().clear();

		List<Book> books = bookRepository.findAll();
		assertThat(books)
			.isNotNull()
			.hasSize(7);
		assertThat(books.get(0).getAuthor().getFirstname()).isNotNull();
		assertThat(books.get(0).getGenre().getName()).isNotNull();
		assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
	}

	@Test
	void shouldCreateBook() {
		List<Book> booksBeforeCreate = bookRepository.findAll();
		assertThat(booksBeforeCreate)
			.isNotNull()
			.hasSize(7);

		Author author = Author.builder()
			.id(1L)
			.firstname("test")
			.lastname("test")
			.build();

		Genre genre = Genre.builder()
			.id(1L)
			.name("Test")
			.build();

		Book book = Book.builder()
			.id(8L)
			.name("Test")
			.genre(genre)
			.author(author)
			.build();

		bookRepository.save(book);

		List<Book> booksAfterCreate = bookRepository.findAll();
		assertThat(booksAfterCreate)
			.isNotNull()
			.hasSize(8);
	}

	@Test
	void shouldUpdateBook() {
		Author author = Author.builder()
			.id(2L)
			.firstname("test")
			.lastname("test")
			.build();

		Genre genre = Genre.builder()
			.id(2L)
			.name("Test")
			.build();

		Book book = Book.builder()
			.id(1L)
			.name("Test")
			.genre(genre)
			.author(author)
			.build();

		Book updatedBook = bookRepository.save(book);
		assertThat(updatedBook.getName()).isEqualTo("Test");
		assertThat(updatedBook.getAuthor().getId()).isEqualTo(2);
		assertThat(updatedBook.getGenre().getId()).isEqualTo(2);
	}

	@Test
	void shouldDeleteBookById() {
		List<Book> booksBeforeCreate = bookRepository.findAll();
		assertThat(booksBeforeCreate)
			.isNotNull()
			.hasSize(7);

		Optional<Book> optionalBook = bookRepository.findById(1L);
		optionalBook.ifPresent(book -> bookRepository.delete(book));

		List<Book> booksAfterCreate = bookRepository.findAll();
		assertThat(booksAfterCreate)
			.isNotNull()
			.hasSize(6);
	}

}
