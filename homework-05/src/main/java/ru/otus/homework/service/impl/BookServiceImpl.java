package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.dao.BookDao;
import ru.otus.homework.domain.Book;
import ru.otus.homework.service.BookService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	private final BookDao bookDao;

	@Override
	public Optional<Book> findBookById(long id) {
		return bookDao.findById(id);
	}

	@Override
	public List<Book> findAllBooks() {
		return bookDao.findAll();
	}

	@Override
	@Transactional
	public Book createBook(Book book) {
		return bookDao.create(book);
	}

	@Override
	@Transactional
	public Book updateBook(Book book) {
		return bookDao.update(book);
	}

	@Override
	@Transactional
	public int deleteBookById(long id) {
		return bookDao.deleteById(id);
	}

}
