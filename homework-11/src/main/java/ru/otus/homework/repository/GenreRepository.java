package ru.otus.homework.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.homework.domain.Genre;

public interface GenreRepository extends ReactiveCrudRepository<Genre, String> {

}
