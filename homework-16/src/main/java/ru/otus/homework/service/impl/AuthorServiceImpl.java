package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.rest.dto.AuthorDto;
import ru.otus.homework.mapper.AuthorMapper;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.domain.Author;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.service.AuthorService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

	private final AuthorRepository authorRepository;

	private final AuthorMapper authorMapper;

	@Override
	@Transactional(readOnly = true)
	public AuthorDto findAuthorById(long id) {
		Author author = authorRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException(
						String.format("Author with id: %d not found", id)));
		return authorMapper.toDto(author);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AuthorDto> findAllAuthors() {
		List<Author> authors = authorRepository.findAll();
		return authorMapper.toDtoList(authors);
	}

	@Override
	@Transactional
	public AuthorDto createAuthor(AuthorDto authorDto) {
		Author newAuthor = authorMapper.toDomain(authorDto);
		authorRepository.save(newAuthor);
		return authorMapper.toDto(newAuthor);
	}

	@Override
	@Transactional
	public AuthorDto updateAuthor(AuthorDto authorDto) {
		Author toUpdate = authorRepository.findById(authorDto.getId()).orElseThrow(
			() -> new DataNotFoundException(
				String.format("Author with id: %s not found", authorDto.getId())));
		String newFirstname = authorDto.getFirstname();
		if (newFirstname != null && !newFirstname.isEmpty()) {
			toUpdate.setFirstname(newFirstname);
		}
		String newPatronymic = authorDto.getPatronymic();
		if (newPatronymic != null && !newPatronymic.isEmpty()) {
			toUpdate.setPatronymic(newPatronymic);
		}
		String newLastname = authorDto.getLastname();
		if (newLastname != null && !newLastname.isEmpty()) {
			toUpdate.setLastname(newLastname);
		}
		authorRepository.save(toUpdate);
		return authorMapper.toDto(toUpdate);
	}

	@Override
	@Transactional
	public void deleteAuthorById(long id) {
		authorRepository.deleteById(id);
	}

}
