package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exception.DataNotPresentException;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.domain.Book;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.service.AuthorService;
import ru.otus.homework.service.BookService;
import ru.otus.homework.service.GenreService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;

	private final AuthorService authorService;

	private final GenreService genreService;

	@Override
	@Transactional(readOnly = true)
	public Optional<Book> findOptionalBookById(long id) {
		return bookRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Book findBookById(long id) {
		return getBook(id);
	}

	private Book getBook(long id) {
		Optional<Book> bookOptional = findOptionalBookById(id);
		return bookOptional.orElseThrow(
			() -> new DataNotFoundException(String.format("Book with id: %d not found", id)));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Book> findAllBooks() {
		return bookRepository.findAll();
	}

	@Override
	@Transactional
	public Book createBook(Book book) {
		book.setAuthor(checkBookAuthor(book));
		book.setGenre(checkBookGenre(book));
		return bookRepository.save(book);
	}

	@Override
	@Transactional
	public Book updateBook(Book book) {
		Book toUpdate = getBook(book.getId());
		toUpdate.setAuthor(checkBookAuthor(book));
		toUpdate.setGenre(checkBookGenre(book));
		String newBookName = book.getName();
		if (newBookName != null && !newBookName.isEmpty()) {
			toUpdate.setName(newBookName);
		}
		return bookRepository.save(toUpdate);
	}

	private Author checkBookAuthor(Book book) {
		if (book.getAuthor() == null || book.getAuthor().getId() == null) {
			throw new DataNotPresentException("Author id is not present");
		}
		return authorService.findAuthorById(book.getAuthor().getId());
	}

	private Genre checkBookGenre(Book book) {
		if (book.getGenre() == null || book.getGenre().getId() == null) {
			throw new DataNotPresentException("Genre id is not present");
		}
		return genreService.findGenreById(book.getGenre().getId());
	}

	@Override
	@Transactional
	public void deleteBookById(long id) {
		Book existBook = getBook(id);
		bookRepository.remove(existBook);
	}

}
