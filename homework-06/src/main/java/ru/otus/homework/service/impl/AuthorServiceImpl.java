package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.domain.Author;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.service.AuthorService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

	private final AuthorRepository authorRepository;

	@Override
	@Transactional(readOnly = true)
	public Author findAuthorById(long id) {
		Optional<Author> authorOptional = authorRepository.findById(id);
		return authorOptional.orElseThrow(
			() -> new DataNotFoundException(String.format("Author with id: %d not found", id)));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Author> findAllAuthors() {
		return authorRepository.findAll();
	}

	@Override
	@Transactional
	public Author createAuthor(Author author) {
		return authorRepository.save(author);
	}

	@Override
	@Transactional
	public Author updateAuthor(Author author) {
		Author toUpdate = findAuthorById(author.getId());
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
		return authorRepository.save(toUpdate);
	}

	@Override
	@Transactional
	public void deleteAuthorById(long id) {
		Author existAuthor = findAuthorById(id);
		authorRepository.remove(existAuthor);
	}

}
