package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.exception.DataNotPresentException;
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

	@Override
	@Transactional(readOnly = true)
	public Book findBookById(long id) {
		Optional<Book> bookOptional = bookRepository.findByIdWithAuthorAndGenre(id);
		return bookOptional.orElseThrow(
			() -> new DataNotFoundException(String.format("Book with id: %d not found", id)));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Book> findAllBooks() {
		return bookRepository.findAllWithAuthorAndGenre();
	}

	@Override
	@Transactional
	public Book createBook(Book book) {
		if (book.getAuthor() == null || book.getAuthor().getId() == null) {
			throw new DataNotPresentException("Author id is not present");
		}
		long authorId = book.getAuthor().getId();
		authorRepository.findById(authorId).ifPresentOrElse(book::setAuthor, () -> {
			throw new DataNotFoundException(String.format("Author with id: %d not found", authorId));
		});

		if (book.getGenre() == null || book.getGenre().getId() == null) {
			throw new DataNotPresentException("Genre id is not present");
		}
		long genreId = book.getGenre().getId();
		genreRepository.findById(genreId).ifPresentOrElse(book::setGenre, () -> {
			throw new DataNotFoundException(String.format("Genre with id: %d not found", genreId));
		});

		return bookRepository.save(book);
	}

	@Override
	@Transactional
	public Book updateBook(Book book) {
		Book toUpdate = bookRepository.findById(book.getId()).orElseThrow(
			() -> new DataNotFoundException(
				String.format("Book with id: %s not found", book.getId())));
		if (book.getAuthor() == null || book.getAuthor().getId() == null) {
			throw new DataNotPresentException("Author id is not present");
		}
		long authorId = book.getAuthor().getId();
		authorRepository.findById(authorId).ifPresentOrElse(toUpdate::setAuthor, () -> {
			throw new DataNotFoundException(String.format("Author with id: %d not found", authorId));
		});
		if (book.getGenre() == null || book.getGenre().getId() == null) {
			throw new DataNotPresentException("Genre id is not present");
		}
		long genreId = book.getGenre().getId();
		genreRepository.findById(genreId).ifPresentOrElse(toUpdate::setGenre, () -> {
			throw new DataNotFoundException(String.format("Genre with id: %d not found", genreId));
		});
		String newBookName = book.getName();
		if (newBookName != null && !newBookName.isEmpty()) {
			toUpdate.setName(newBookName);
		}
		return bookRepository.save(toUpdate);
	}

	@Override
	@Transactional
	public void deleteBookById(long id) {
		bookRepository.deleteById(id);
	}

}
