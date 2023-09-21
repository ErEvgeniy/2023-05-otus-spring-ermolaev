package ru.otus.homework.rest.endpoint;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.mapper.BookMapper;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.CommentRepository;
import ru.otus.homework.repository.GenreRepository;
import ru.otus.homework.rest.dto.BookDto;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class BookEndpoint {

    @Bean
    public RouterFunction<ServerResponse> bookRoutes(
            AuthorRepository authorRepository,
            BookRepository bookRepository,
            GenreRepository genreRepository,
            CommentRepository commentRepository,
            BookMapper bookMapper
    ) {
        BookHandler handler = new BookHandler(authorRepository, bookRepository,
                genreRepository, commentRepository, bookMapper);

        return route()
                .GET("/library/v1/book", accept(APPLICATION_JSON), handler::list)
                .POST("/library/v1/book", accept(APPLICATION_JSON), handler::create)
                .PATCH("/library/v1/book/{id}", accept(APPLICATION_JSON), handler::update)
                .DELETE("/library/v1/book/{id}", accept(APPLICATION_JSON), handler::delete)
                .build();
    }

    private static class BookHandler {

        private final BookRepository bookRepository;

        private final AuthorRepository authorRepository;

        private final GenreRepository genreRepository;

        private final CommentRepository commentRepository;

        private final BookMapper bookMapper;

        public BookHandler(AuthorRepository authorRepository,
                           BookRepository bookRepository,
                           GenreRepository genreRepository,
                           CommentRepository commentRepository,
                           BookMapper bookMapper) {
            this.authorRepository = authorRepository;
            this.bookRepository = bookRepository;
            this.genreRepository = genreRepository;
            this.commentRepository = commentRepository;
            this.bookMapper = bookMapper;
        }

        public Mono<ServerResponse> list(ServerRequest request) {
            return ok().contentType(APPLICATION_JSON)
                    .body(bookRepository.
                            findAll()
                            .map(bookMapper::toDto), BookDto.class);
        }

        public Mono<ServerResponse> create(ServerRequest request) {
            return request.bodyToMono(BookDto.class)
                    .map(bookMapper::toDomain)
                    .flatMap(book -> authorRepository.findById(book.getAuthor().getId())
                            .switchIfEmpty(Mono.error(new DataNotFoundException(
                                    String.format("Author with id: %s not found", book.getAuthor().getId()))))
                            .flatMap(author -> {
                                book.setAuthor(author);
                                return Mono.just(book);
                            })
                    )
                    .flatMap(book -> genreRepository.findById(book.getGenre().getId())
                            .switchIfEmpty(Mono.error(new DataNotFoundException(
                                    String.format("Genre with id: %s not found", book.getGenre().getId()))))
                            .flatMap(genre -> {
                                book.setGenre(genre);
                                return Mono.just(book);
                            })
                    )
                    .flatMap(bookRepository::save)
                    .map(bookMapper::toDto)
                    .transform(dto -> ok().contentType(APPLICATION_JSON).body(dto, BookDto.class));
        }

        public Mono<ServerResponse> update(ServerRequest request) {
            String bookId = request.pathVariable("id");
            Mono<Book> existingBookMono = bookRepository.findById(bookId);
            Mono<BookDto> bookDtoMono = request.bodyToMono(BookDto.class);

            Mono<ServerResponse> notFoundResponse = ServerResponse.notFound().build();
            Mono<DataNotFoundException> notFoundException = Mono.error(new DataNotFoundException(
                    String.format("Book with id: %s not found", bookId)));

            Mono<BookDto> responseMono = Mono.zip(existingBookMono, bookDtoMono)
                    .switchIfEmpty(notFoundException.flatMap(Mono::error))
                    .flatMap(this::updateBookWithAuthorAndGenre)
                    .flatMap(tuple -> {
                        tuple.getT1().setName(tuple.getT2().getName());
                        return bookRepository.save(tuple.getT1());
                    })
                    .map(bookMapper::toDto);

            return responseMono
                    .flatMap(updatedBookDto -> ServerResponse.ok()
                            .contentType(APPLICATION_JSON)
                            .bodyValue(updatedBookDto))
                    .switchIfEmpty(notFoundResponse);
        }

        private Mono<Tuple2<Book, BookDto>> updateBookWithAuthorAndGenre(Tuple2<Book, BookDto> tuple) {
            String authorId = tuple.getT2().getAuthor().getId();
            String genreId = tuple.getT2().getGenre().getId();

            Mono<Author> authorMono = authorRepository.findById(authorId)
                    .switchIfEmpty(Mono.error(new DataNotFoundException(
                            String.format("Author with id: %s not found", authorId))))
                    .flatMap(author -> {
                        tuple.getT1().setAuthor(author);
                        return Mono.just(author);
                    });

            Mono<Genre> genreMono = genreRepository.findById(genreId)
                    .switchIfEmpty(Mono.error(new DataNotFoundException(
                            String.format("Genre with id: %s not found", genreId))))
                    .flatMap(genre -> {
                        tuple.getT1().setGenre(genre);
                        return Mono.just(genre);
                    });

            return Mono.when(authorMono, genreMono)
                    .thenReturn(tuple);
        }

        public Mono<ServerResponse> delete(ServerRequest request) {
            String bookId = request.pathVariable("id");
            return bookRepository
                    .deleteById(bookId)
                    .then(Mono.defer(() -> commentRepository.deleteAllByBookId(bookId)))
                    .then(ok().contentType(APPLICATION_JSON).build());
        }
    }
}
