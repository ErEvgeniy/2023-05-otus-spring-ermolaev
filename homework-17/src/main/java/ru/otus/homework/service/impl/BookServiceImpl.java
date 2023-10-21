package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
	public Book findBookById(String id) {
		Optional<Book> bookOptional = bookRepository.findById(id);
		return bookOptional.orElseThrow(
			() -> new DataNotFoundException(String.format("Book with id: %s not found", id)));
	}

	@Override
	public List<Book> findAllBooks() {
		return bookRepository.findAll();
	}

	@Override
	public Book createBook(Book book) {
		if (book.getAuthor() == null || book.getAuthor().getId() == null) {
			throw new DataNotPresentException("Author id is not present");
		}
		String authorId = book.getAuthor().getId();
		authorRepository.findById(authorId).ifPresentOrElse(book::setAuthor, () -> {
			throw new DataNotFoundException(String.format("Author with id: %s not found", authorId));
		});

		if (book.getGenre() == null || book.getGenre().getId() == null) {
			throw new DataNotPresentException("Genre id is not present");
		}
		String genreId = book.getGenre().getId();
		genreRepository.findById(genreId).ifPresentOrElse(book::setGenre, () -> {
			throw new DataNotFoundException(String.format("Genre with id: %s not found", genreId));
		});

		return bookRepository.save(book);
	}

	@Override
	public Book updateBook(Book book) {
		Book toUpdate = bookRepository.findById(book.getId()).orElseThrow(
			() -> new DataNotFoundException(
				String.format("Book with id: %s not found", book.getId())));
		if (book.getAuthor() != null && book.getAuthor().getId() != null) {
			String authorId = book.getAuthor().getId();
			authorRepository.findById(authorId).ifPresentOrElse(toUpdate::setAuthor, () -> {
				throw new DataNotFoundException(
					String.format("Author with id: %s not found", authorId));
			});
		}
		if (book.getGenre() != null && book.getGenre().getId() != null) {
			String genreId = book.getGenre().getId();
			genreRepository.findById(genreId).ifPresentOrElse(toUpdate::setGenre, () -> {
				throw new DataNotFoundException(
					String.format("Genre with id: %s not found", genreId));
			});
		}
		String newBookName = book.getName();
		if (newBookName != null && !newBookName.isEmpty()) {
			toUpdate.setName(newBookName);
		}
		return bookRepository.save(toUpdate);
	}

	@Override
	public void deleteBookById(String id) {
		bookRepository.deleteById(id);
	}

}
