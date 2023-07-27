package ru.otus.homework.repository;

import ru.otus.homework.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends Repository<Genre> {

	List<Genre> findAll();

	Optional<Genre> findById(Long id);

}
