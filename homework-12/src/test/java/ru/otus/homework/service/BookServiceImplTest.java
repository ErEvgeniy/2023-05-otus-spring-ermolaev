package ru.otus.homework.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.dto.AuthorDto;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.dto.GenreDto;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.mapper.BookMapper;
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

@SpringBootTest(classes = BookServiceImpl.class)
class BookServiceImplTest {

	private static final String BOOK_NAME = "Test";

	private static final Long BOOK_ID = 1L;

	private static final Long AUTHOR_ID = 2L;

	private static final Long GENRE_ID = 3L;

	@MockBean
	private BookRepository bookRepository;

	@MockBean
	private AuthorRepository authorRepository;

	@MockBean
	private GenreRepository genreRepository;

	@MockBean
	private BookMapper bookMapper;

	@Autowired
	private BookServiceImpl bookService;

	@Test
	void shouldFindOneBookById() {
		Book dummyBook = getDummyBook();
		when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(dummyBook));
		when(bookMapper.toDto(dummyBook)).thenReturn(getDummyDtoBook());

		BookDto book = bookService.findBookById(BOOK_ID);

		assertThat(book).isNotNull();
		assertThat(book.getId()).isEqualTo(BOOK_ID);
		assertThat(book.getName()).isEqualTo(BOOK_NAME);
		assertThat(book.getAuthor().getId()).isEqualTo(AUTHOR_ID);
		assertThat(book.getGenre().getId()).isEqualTo(GENRE_ID);

		verify(bookRepository, times(1)).findById(BOOK_ID);
		verify(bookMapper, times(1)).toDto(dummyBook);
	}

	@Test
	void shouldNotFindBookById() {
		when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.empty());

		assertThrows(DataNotFoundException.class, () -> bookService.findBookById(BOOK_ID));

		verify(bookRepository, times(1)).findById(BOOK_ID);
	}

	@Test
	void shouldFindAllBooks() {
		List<Book> dummyBooks = List.of(getDummyBook());
		when(bookRepository.findAll()).thenReturn(dummyBooks);
		when(bookMapper.toDtoList(dummyBooks)).thenReturn(List.of(getDummyDtoBook()));


		List<BookDto> book = bookService.findAllBooks();

		assertThat(book)
			.isNotNull()
			.hasSize(1);

		verify(bookRepository, times(1)).findAll();
		verify(bookMapper, times(1)).toDtoList(dummyBooks);
	}

	@Test
	void shouldCreateBook() {
		Book bookToCreate = getDummyBook();
		BookDto dummyDto = getDummyDtoBook();
		when(bookMapper.toDomain(dummyDto)).thenReturn(bookToCreate);
		when(bookRepository.save(bookToCreate)).thenReturn(bookToCreate);
		when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(getDummyAuthor()));
		when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(getDummyGenre()));
		when(bookMapper.toDto(bookToCreate)).thenReturn(dummyDto);
		BookDto book = bookService.createBook(dummyDto);

		assertThat(book)
			.isNotNull()
			.isEqualTo(dummyDto);

		verify(authorRepository, times(1)).findById(AUTHOR_ID);
		verify(genreRepository, times(1)).findById(GENRE_ID);
		verify(bookRepository, times(1)).save(bookToCreate);
		verify(bookMapper, times(1)).toDomain(dummyDto);
		verify(bookMapper, times(1)).toDto(bookToCreate);
	}

	@Test
	void shouldUpdateBook() {
		Book bookToUpdate = getDummyBook();
		BookDto dummyDto = getDummyDtoBook();

		when(bookRepository.save(bookToUpdate)).thenReturn(bookToUpdate);
		when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(bookToUpdate));
		when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(getDummyAuthor()));
		when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(getDummyGenre()));
		when(bookMapper.toDto(bookToUpdate)).thenReturn(dummyDto);
		BookDto book = bookService.updateBook(dummyDto);

		assertThat(book)
			.isNotNull()
			.isEqualTo(dummyDto);

		verify(bookRepository, times(1)).findById(BOOK_ID);
		verify(authorRepository, times(1)).findById(AUTHOR_ID);
		verify(genreRepository, times(1)).findById(GENRE_ID);
		verify(bookRepository, times(1)).save(bookToUpdate);
		verify(bookMapper, times(1)).toDto(bookToUpdate);
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

	private BookDto getDummyDtoBook() {
		AuthorDto author = getDummyDtoAuthor();
		GenreDto genre = getDummyDtoGenre();

		BookDto bookDto = new BookDto();
		bookDto.setId(BOOK_ID);
		bookDto.setName(BOOK_NAME);
		bookDto.setAuthor(author);
		bookDto.setGenre(genre);
		return bookDto;
	}

	private AuthorDto getDummyDtoAuthor() {
		AuthorDto authorDto = new AuthorDto();
		authorDto.setId(AUTHOR_ID);
		return authorDto;
	}

	private GenreDto getDummyDtoGenre() {
		GenreDto genreDto = new GenreDto();
		genreDto.setId(GENRE_ID);
		return genreDto;
	}

}
