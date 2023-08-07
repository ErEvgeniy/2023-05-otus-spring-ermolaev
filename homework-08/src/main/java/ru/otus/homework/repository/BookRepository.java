package ru.otus.homework.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.domain.Book;

import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {

	boolean existsBookByAuthorId(String authorId);

	boolean existsBookByGenreId(String genreId);

	Optional<Book> findBookByCommentsIs(String commentId);

}
