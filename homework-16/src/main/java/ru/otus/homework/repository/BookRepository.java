package ru.otus.homework.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.homework.domain.Book;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "book")
public interface BookRepository extends JpaRepository<Book, Long> {

	@EntityGraph(value = "bookWithGenreAndAuthor", type = EntityGraph.EntityGraphType.LOAD)
	Optional<Book> findById(Long id);

	@EntityGraph(value = "bookWithGenreAndAuthor", type = EntityGraph.EntityGraphType.LOAD)
	List<Book> findAll();

}
