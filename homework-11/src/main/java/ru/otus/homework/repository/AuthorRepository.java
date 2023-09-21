package ru.otus.homework.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.homework.domain.Author;

public interface AuthorRepository extends ReactiveCrudRepository<Author, String> {

}
