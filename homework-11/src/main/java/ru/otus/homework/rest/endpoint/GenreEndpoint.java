package ru.otus.homework.rest.endpoint;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exception.NonConsistentOperation;
import ru.otus.homework.mapper.GenreMapper;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.GenreRepository;
import ru.otus.homework.rest.dto.GenreDto;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class GenreEndpoint {

    @Bean
    public RouterFunction<ServerResponse> genreRoutes(
            GenreRepository genreRepository,
            BookRepository bookRepository,
            GenreMapper genreMapper
    ) {
        GenreHandler handler = new GenreHandler(genreRepository, bookRepository, genreMapper);

        return route()
                .GET("/library/v1/genre", accept(APPLICATION_JSON), handler::list)
                .POST("/library/v1/genre", accept(APPLICATION_JSON), handler::create)
                .PATCH("/library/v1/genre/{id}", accept(APPLICATION_JSON), handler::update)
                .DELETE("/library/v1/genre/{id}", accept(APPLICATION_JSON), handler::delete)
                .build();
    }

    private static class GenreHandler {

        private final GenreRepository genreRepository;

        private final BookRepository bookRepository;

        private final GenreMapper genreMapper;

        public GenreHandler(GenreRepository genreRepository,
                            BookRepository bookRepository,
                            GenreMapper genreMapper) {
            this.genreRepository = genreRepository;
            this.bookRepository = bookRepository;
            this.genreMapper = genreMapper;
        }

        Mono<ServerResponse> list(ServerRequest request) {
            return ok().contentType(APPLICATION_JSON)
                    .body(genreRepository.
                            findAll()
                            .map(genreMapper::toDto), GenreDto.class);
        }

        Mono<ServerResponse> create(ServerRequest request) {
            return request.bodyToMono(GenreDto.class)
                    .map(genreMapper::toDomain)
                    .flatMap(genreRepository::save)
                    .map(genreMapper::toDto)
                    .transform(dto -> ok().contentType(APPLICATION_JSON).body(dto, GenreDto.class));
        }

        Mono<ServerResponse> update(ServerRequest request) {
            String genreId = request.pathVariable("id");
            Mono<Genre> existingGenreMono = genreRepository.findById(genreId);
            Mono<GenreDto> genreDtoMono = request.bodyToMono(GenreDto.class);

            return existingGenreMono
                    .zipWith(genreDtoMono)
                    .flatMap(tuple -> {
                        Genre existingGenre = tuple.getT1();
                        GenreDto genreDto = tuple.getT2();

                        existingGenre.setName(genreDto.getName());

                        return genreRepository.save(existingGenre);
                    })
                    .map(genreMapper::toDto)
                    .flatMap(updatedGenreDto -> ServerResponse.ok()
                            .contentType(APPLICATION_JSON)
                            .bodyValue(updatedGenreDto))
                    .switchIfEmpty(ServerResponse.notFound().build());
        }

        Mono<ServerResponse> delete(ServerRequest request) {
            return bookRepository
                    .findBooksByGenreId(request.pathVariable("id"))
                    .flatMap(book -> Mono.error(new NonConsistentOperation(
                            String.format("Genre with id %s is used", request.pathVariable("id")))))
                    .switchIfEmpty(genreRepository.deleteById(request.pathVariable("id")))
                    .then(ok().contentType(APPLICATION_JSON).build());
        }
    }
}
