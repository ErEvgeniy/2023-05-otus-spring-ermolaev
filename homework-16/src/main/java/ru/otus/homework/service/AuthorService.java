package ru.otus.homework.service;

import ru.otus.homework.rest.dto.AuthorDto;

import java.util.List;

public interface AuthorService {

	AuthorDto findAuthorById(long id);

	List<AuthorDto> findAllAuthors();

	AuthorDto createAuthor(AuthorDto author);

	AuthorDto updateAuthor(AuthorDto author);

	void deleteAuthorById(long id);

}
