package ru.otus.homework.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

	@EntityGraph(value = "bookWithGenreAndAuthor", type = EntityGraph.EntityGraphType.LOAD)
	Optional<Book> findById(Long id);

	@EntityGraph(value = "bookWithGenreAndAuthor", type = EntityGraph.EntityGraphType.LOAD)
	List<Book> findAll();

}
