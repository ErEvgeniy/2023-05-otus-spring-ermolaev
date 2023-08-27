package ru.otus.homework.rest.endpoint;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.mapper.CommentMapper;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.CommentRepository;
import ru.otus.homework.rest.dto.CommentDto;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class CommentEndpoint {

    @Bean
    public RouterFunction<ServerResponse> commentRoutes(
            CommentRepository commentRepository,
            BookRepository bookRepository,
            CommentMapper commentMapper
    ) {
        CommentHandler handler = new CommentHandler(commentRepository, bookRepository, commentMapper);

        return route()
                .GET("/library/v1/comment/book/{bookId}", accept(APPLICATION_JSON), handler::list)
                .POST("/library/v1/comment", accept(APPLICATION_JSON), handler::create)
                .PATCH("/library/v1/comment/{id}", accept(APPLICATION_JSON), handler::update)
                .DELETE("/library/v1/comment/{id}", accept(APPLICATION_JSON), handler::delete)
                .build();
    }

    private static class CommentHandler {

        private final CommentRepository commentRepository;

        private final BookRepository bookRepository;

        private final CommentMapper commentMapper;

        public CommentHandler(CommentRepository commentRepository,
                              BookRepository bookRepository,
                              CommentMapper commentMapper) {
            this.commentRepository = commentRepository;
            this.bookRepository = bookRepository;
            this.commentMapper = commentMapper;
        }

        Mono<ServerResponse> list(ServerRequest request) {
            return ok().contentType(APPLICATION_JSON)
                    .body(commentRepository.
                            findAllByBookId(request.pathVariable("bookId"))
                            .map(commentMapper::toDto), CommentDto.class);
        }

        Mono<ServerResponse> create(ServerRequest request) {
            return request.bodyToMono(CommentDto.class)
                    .flatMap(dto -> bookRepository
                            .findById(dto.getBookId())
                            .switchIfEmpty(Mono.error(new DataNotFoundException(
                                    String.format("Book with id: %s not found", dto.getBookId())
                            )))
                            .flatMap(book -> {
                                Comment comment = commentMapper.toDomain(dto);
                                comment.setBook(book);
                                book.getComments().add(comment);
                                return commentRepository.save(comment).thenReturn(comment);
                            })
                            .flatMap(comment -> bookRepository.save(comment.getBook()).thenReturn(comment))
                    )
                    .map(commentMapper::toDto)
                    .flatMap(dto -> ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(dto))
                    .switchIfEmpty(ServerResponse.badRequest().build());
        }

        Mono<ServerResponse> update(ServerRequest request) {
            String commentId = request.pathVariable("id");
            Mono<Comment> existingCommentMono = commentRepository.findById(commentId);
            Mono<CommentDto> commentDtoMono = request.bodyToMono(CommentDto.class);

            return existingCommentMono
                    .zipWith(commentDtoMono)
                    .flatMap(tuple -> {
                        Comment existingComment = tuple.getT1();
                        CommentDto commentDto = tuple.getT2();

                        existingComment.setText(commentDto.getText());

                        return commentRepository.save(existingComment);
                    })
                    .map(commentMapper::toDto)
                    .flatMap(updatedCommentDto -> ServerResponse.ok()
                            .contentType(APPLICATION_JSON)
                            .bodyValue(updatedCommentDto))
                    .switchIfEmpty(ServerResponse.notFound().build());
        }

        Mono<ServerResponse> delete(ServerRequest request) {
            String commentId = request.pathVariable("id");

            Mono<Comment> commentMono = commentRepository.findById(commentId);

            return commentMono
                    .flatMap(comment -> {
                        String bookId = comment.getBook().getId();
                        Mono<Book> bookMono = bookRepository.findById(bookId);

                        return bookMono
                                .flatMap(book -> {
                                    book.getComments().removeIf(c -> c.getId().equals(commentId));
                                    return bookRepository.save(book);
                                })
                                .then(commentRepository.deleteById(commentId))
                                .then(ServerResponse.ok().build());
                    })
                    .switchIfEmpty(ServerResponse.notFound().build());
        }
    }
}
