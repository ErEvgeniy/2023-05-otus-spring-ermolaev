package ru.otus.homework.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.otus.homework.domain.Book;

public interface BookRepository extends ReactiveCrudRepository<Book, String> {

	Flux<Book> findBooksByAuthorId(String authorId);

	Flux<Book> findBooksByGenreId(String genreId);

}
