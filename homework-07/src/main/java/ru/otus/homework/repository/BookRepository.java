package ru.otus.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.homework.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

	@Query("SELECT b FROM Book b JOIN FETCH b.author JOIN FETCH b.genre WHERE b.id = :bookId")
	Optional<Book> findByIdWithAuthorAndGenre(@Param("bookId") long bookId);

	@Query("SELECT b FROM Book b JOIN FETCH b.author JOIN FETCH b.genre")
	List<Book> findAllWithAuthorAndGenre();

}
