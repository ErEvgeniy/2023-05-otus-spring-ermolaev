package ru.otus.homework.rest.endpoint;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.homework.domain.Author;
import ru.otus.homework.exception.NonConsistentOperation;
import ru.otus.homework.mapper.AuthorMapper;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.rest.dto.AuthorDto;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class AuthorEndpoint {

    @Bean
    public RouterFunction<ServerResponse> authorRoutes(
            AuthorRepository authorRepository,
            BookRepository bookRepository,
            AuthorMapper authorMapper
    ) {
        AuthorHandler handler = new AuthorHandler(authorRepository, bookRepository, authorMapper);

        return route()
                .GET("/library/v1/author", accept(APPLICATION_JSON), handler::list)
                .POST("/library/v1/author", accept(APPLICATION_JSON), handler::create)
                .PATCH("/library/v1/author/{id}", accept(APPLICATION_JSON), handler::update)
                .DELETE("/library/v1/author/{id}", accept(APPLICATION_JSON), handler::delete)
                .build();
    }

    private static class AuthorHandler {

        private final AuthorRepository authorRepository;

        private final BookRepository bookRepository;

        private final AuthorMapper authorMapper;

        public AuthorHandler(AuthorRepository authorRepository,
                             BookRepository bookRepository,
                             AuthorMapper authorMapper) {
            this.authorRepository = authorRepository;
            this.bookRepository = bookRepository;
            this.authorMapper = authorMapper;
        }

        Mono<ServerResponse> list(ServerRequest request) {
            return ok().contentType(APPLICATION_JSON)
                    .body(authorRepository.
                            findAll()
                            .map(authorMapper::toDto), AuthorDto.class);
        }

        Mono<ServerResponse> create(ServerRequest request) {
            return request.bodyToMono(AuthorDto.class)
                    .map(authorMapper::toDomain)
                    .flatMap(authorRepository::save)
                    .map(authorMapper::toDto)
                    .transform(dto -> ok().contentType(APPLICATION_JSON).body(dto, AuthorDto.class));
        }

        Mono<ServerResponse> update(ServerRequest request) {
            String authorId = request.pathVariable("id");
            Mono<Author> existingAuthorMono = authorRepository.findById(authorId);
            Mono<AuthorDto> authorDtoMono = request.bodyToMono(AuthorDto.class);

            return existingAuthorMono
                    .zipWith(authorDtoMono)
                    .flatMap(tuple -> {
                        Author existingAuthor = tuple.getT1();
                        AuthorDto authorDto = tuple.getT2();

                        existingAuthor.setFirstname(authorDto.getFirstname());
                        existingAuthor.setPatronymic(authorDto.getPatronymic());
                        existingAuthor.setLastname(authorDto.getLastname());

                        return authorRepository.save(existingAuthor);
                    })
                    .map(authorMapper::toDto)
                    .flatMap(updatedAuthorDto -> ServerResponse.ok()
                            .contentType(APPLICATION_JSON)
                            .bodyValue(updatedAuthorDto))
                    .switchIfEmpty(ServerResponse.notFound().build());
        }

        Mono<ServerResponse> delete(ServerRequest request) {
            return bookRepository
                    .findBooksByAuthorId(request.pathVariable("id"))
                    .flatMap(book -> Mono.error(new NonConsistentOperation(
                            String.format("Author with id %s is used", request.pathVariable("id")))))
                    .switchIfEmpty(authorRepository.deleteById(request.pathVariable("id")))
                    .then(ok().contentType(APPLICATION_JSON).build());
        }
    }
}
