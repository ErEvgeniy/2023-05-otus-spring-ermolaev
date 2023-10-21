package ru.otus.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.homework.domain.Genre;

@RepositoryRestResource(path = "genre")
public interface GenreRepository extends JpaRepository<Genre, Long> {

}
