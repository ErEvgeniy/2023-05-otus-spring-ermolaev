package ru.otus.homework.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.Index;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.GenreRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "eermolaev", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "eermolaev")
    public void insertAuthors(AuthorRepository authorRepository) {
        authorRepository.insert(List.of(
            new Author(null, "ALEKSANDR", "SERGEEVICH", "PUSHKIN"),
            new Author(null, "NIKOLAY", "VASILEVICH", "GOGOL"),
            new Author(null, "LEV", "NIKOLAEVICH", "TOLSTOY")
        ));
    }

    @ChangeSet(order = "003", id = "createGenreIndexes", author = "eermolaev")
    public void createGenreIndexes(MongockTemplate mongoTemplate) {
        mongoTemplate.indexOps("genres")
            .ensureIndex(new Index().on("name", Sort.Direction.ASC).unique());
    }

    @ChangeSet(order = "004", id = "insertGenres", author = "eermolaev")
    public void insertGenres(GenreRepository genreRepository) {
        genreRepository.insert(List.of(
            new Genre(null, "STORY"),
            new Genre(null, "PLAY"),
            new Genre(null, "NOVEL")
        ));
    }

    @ChangeSet(order = "005", id = "insertBooks", author = "eermolaev")
    public void insertBooks(
        BookRepository bookRepository,
        AuthorRepository authorRepository,
        GenreRepository genreRepository
    ) {
        Map<String, Author> authors = getAuthors(authorRepository);
        Map<String, Genre> genres = getGenres(genreRepository);

        bookRepository.insert(List.of(
            new Book(null, "EVGENIY ONEGIN", genres.get("NOVEL"), authors.get("PUSHKIN"), List.of(
                new Comment(UUID.randomUUID().toString(), "COMMENTARY 1 FOR: EVGENIY ONEGIN"),
                new Comment(UUID.randomUUID().toString(), "COMMENTARY 2 FOR: EVGENIY ONEGIN"),
                new Comment(UUID.randomUUID().toString(), "COMMENTARY 3 FOR: EVGENIY ONEGIN")
            )),
            new Book(null, "QUEEN OF SPADES", genres.get("STORY"), authors.get("PUSHKIN"), List.of(
                new Comment(UUID.randomUUID().toString(), "COMMENTARY FOR: QUEEN OF SPADES")
            )),
            new Book(null, "VIY", genres.get("STORY"), authors.get("GOGOL"), List.of(
                new Comment(UUID.randomUUID().toString(), "COMMENTARY FOR: VIY")
            )),
            new Book(null, "SHINEL", genres.get("STORY"), authors.get("GOGOL"), List.of(
                new Comment(UUID.randomUUID().toString(), "COMMENTARY FOR: SHINEL")
            )),
            new Book(null, "THE GOVERNMENT INSPECTOR", genres.get("PLAY"), authors.get("GOGOL"), List.of(
                new Comment(UUID.randomUUID().toString(), "COMMENTARY FOR: THE GOVERNMENT INSPECTOR")
            ))
        ));
    }

    private Map<String, Author> getAuthors(AuthorRepository authorRepository) {
        return authorRepository.findAll().stream()
            .collect(Collectors.toMap(Author::getLastname, Function.identity()));
    }

    private Map<String, Genre> getGenres(GenreRepository genreRepository) {
        return genreRepository.findAll().stream()
            .collect(Collectors.toMap(Genre::getName, Function.identity()));
    }

}
