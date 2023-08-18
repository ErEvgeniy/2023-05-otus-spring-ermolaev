package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.exception.DataNotPresentException;
import ru.otus.homework.mapper.BookMapper;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.domain.Book;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.repository.GenreRepository;
import ru.otus.homework.service.BookService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;

	private final AuthorRepository authorRepository;

	private final GenreRepository genreRepository;

	private final BookMapper bookMapper;

	@Override
	@Transactional(readOnly = true)
	public BookDto findBookById(long id) {
		Optional<Book> bookOptional = bookRepository.findById(id);
		if (bookOptional.isEmpty()) {
			throw new DataNotFoundException(String.format("Book with id: %d not found", id));
		}
		return bookMapper.toDto(bookOptional.get());
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookDto> findAllBooks() {
		List<Book> books = bookRepository.findAll();
		return bookMapper.toDtoList(books);
	}

	@Override
	@Transactional
	public BookDto createBook(BookDto bookDto) {
		Book newBook = bookMapper.toDomain(bookDto);
		if (bookDto.getAuthor() == null || bookDto.getAuthor().getId() == null) {
			throw new DataNotPresentException("Author id is not present");
		}
		long authorId = bookDto.getAuthor().getId();
		authorRepository.findById(authorId)
			.ifPresentOrElse(newBook::setAuthor, () -> {
				throw new DataNotFoundException(String.format("Author with id: %d not found", authorId));
			});

		if (bookDto.getGenre() == null || bookDto.getGenre().getId() == null) {
			throw new DataNotPresentException("Genre id is not present");
		}
		long genreId = bookDto.getGenre().getId();
		genreRepository.findById(genreId)
			.ifPresentOrElse(newBook::setGenre, () -> {
				throw new DataNotFoundException(String.format("Genre with id: %d not found", genreId));
			});

		bookRepository.save(newBook);
		return bookMapper.toDto(newBook);
	}

	@Override
	@Transactional
	public BookDto updateBook(BookDto bookDto) {
		Book toUpdate = bookRepository.findById(bookDto.getId()).orElseThrow(
			() -> new DataNotFoundException(
				String.format("Book with id: %s not found", bookDto.getId())));
		if (bookDto.getAuthor() == null || bookDto.getAuthor().getId() == null) {
			throw new DataNotPresentException("Author id is not present");
		}
		long authorId = bookDto.getAuthor().getId();
		authorRepository.findById(authorId).ifPresentOrElse(toUpdate::setAuthor, () -> {
			throw new DataNotFoundException(String.format("Author with id: %d not found", authorId));
		});
		if (bookDto.getGenre() == null || bookDto.getGenre().getId() == null) {
			throw new DataNotPresentException("Genre id is not present");
		}
		long genreId = bookDto.getGenre().getId();
		genreRepository.findById(genreId).ifPresentOrElse(toUpdate::setGenre, () -> {
			throw new DataNotFoundException(String.format("Genre with id: %d not found", genreId));
		});
		String newBookName = bookDto.getName();
		if (newBookName != null && !newBookName.isEmpty()) {
			toUpdate.setName(newBookName);
		}

		bookRepository.save(toUpdate);
		return bookMapper.toDto(toUpdate);
	}

	@Override
	@Transactional
	public void deleteBookById(long id) {
		bookRepository.deleteById(id);
	}

}
