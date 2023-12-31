package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.dao.AuthorDao;
import ru.otus.homework.domain.Author;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.service.AuthorService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

	private final AuthorDao authorDao;

	@Override
	@Transactional(readOnly = true)
	public Optional<Author> findOptionalAuthorById(long id) {
		return authorDao.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Author findAuthorById(long id) {
		return getAuthor(id);
	}

	private Author getAuthor(long id) {
		Optional<Author> authorOptional = findOptionalAuthorById(id);
		return authorOptional.orElseThrow(
			() -> new DataNotFoundException(String.format("Author with id: %d not found", id)));
	}

	@Override
	@Transactional(readOnly = true)
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
		Author toUpdate = getAuthor(author.getId());
		String newFirstname = author.getFirstname();
		if (newFirstname != null && !newFirstname.isEmpty()) {
			toUpdate.setFirstname(newFirstname);
		}
		String newPatronymic = author.getPatronymic();
		if (newPatronymic != null && !newPatronymic.isEmpty()) {
			toUpdate.setPatronymic(newPatronymic);
		}
		String newLastname = author.getLastname();
		if (newLastname != null && !newLastname.isEmpty()) {
			toUpdate.setLastname(newLastname);
		}
		return authorDao.update(toUpdate);
	}

	@Override
	@Transactional
	public void deleteAuthorById(long id) {
		authorDao.deleteById(id);
	}

}
