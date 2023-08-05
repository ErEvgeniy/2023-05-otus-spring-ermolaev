package ru.otus.homework.repository;

import ru.otus.homework.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends Repository<Author> {

	List<Author> findAll();

	Optional<Author> findById(Long id);

}
