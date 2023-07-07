package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.dao.AuthorDao;
import ru.otus.homework.domain.Author;
import ru.otus.homework.service.AuthorService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

	private final AuthorDao authorDao;

	@Override
	public Optional<Author> findAuthorById(long id) {
		return authorDao.findById(id);
	}

	@Override
	public List<Author> findAllAuthors() {
		return authorDao.findAll();
	}

	@Override
	@Transactional
	public Author createAuthor(Author author) {
		return authorDao.create(author);
	}

	@Override
	@Transactional
	public Author updateAuthor(Author author) {
		return authorDao.update(author);
	}

	@Override
	@Transactional
	public int deleteAuthorById(long id) {
		return authorDao.deleteById(id);
	}

}
