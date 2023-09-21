package ru.otus.homework.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.domain.Comment;

public interface CommentRepository extends ReactiveCrudRepository<Comment, String> {

	Flux<Comment> findAllByBookId(String bookId);

	Mono<Void> deleteAllByBookId(String bookId);

}
