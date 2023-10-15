package ru.otus.homework.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import ru.otus.homework.model.Author;
import ru.otus.homework.model.Book;
import ru.otus.homework.model.Genre;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ChangeLog
public class DatabaseChangelog {

    private static final String ONEGIN = "EVGENIY ONEGIN";

    private static final String QUEEN_OF_SPADES = "QUEEN OF SPADES";

    private static final String VIY = "VIY";

    private static final String SHINEL = "SHINEL";

    private static final String THE_GOVERNMENT_INSPECTOR = "THE GOVERNMENT INSPECTOR";

    private static final String NOVEL = "NOVEL";

    private static final String STORY = "STORY";

    private static final String PUSHKIN = "PUSHKIN";

    private static final String GOGOL = "GOGOL";

    private static final String TOLSTOY = "TOLSTOY";

    private static final String PLAY = "PLAY";

    @ChangeSet(order = "001", id = "dropDb", author = "eermolaev", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "eermolaev")
    public void insertAuthors(MongockTemplate template) {
        template.insertAll(List.of(
            new Author(null, "ALEKSANDR", "SERGEEVICH", PUSHKIN),
            new Author(null, "NIKOLAY", "VASILEVICH", GOGOL),
            new Author(null, "LEV", "NIKOLAEVICH", TOLSTOY)
        ));
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "eermolaev")
    public void insertGenres(MongockTemplate template) {
        template.insertAll(List.of(
            new Genre(null, STORY),
            new Genre(null, PLAY),
            new Genre(null, NOVEL)
        ));
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "eermolaev")
    public void insertBooks(MongockTemplate template) {
        Map<String, Author> authors = getAuthors(template);
        Map<String, Genre> genres = getGenres(template);

        template.insertAll(List.of(
            new Book(null, ONEGIN, genres.get(NOVEL), authors.get(PUSHKIN)),
            new Book(null, QUEEN_OF_SPADES, genres.get(STORY), authors.get(PUSHKIN)),
            new Book(null, VIY, genres.get(STORY), authors.get(GOGOL)),
            new Book(null, SHINEL, genres.get(STORY), authors.get(GOGOL)),
            new Book(null, THE_GOVERNMENT_INSPECTOR, genres.get(PLAY), authors.get(GOGOL))
        ));
    }

    private Map<String, Author> getAuthors(MongockTemplate template) {
        return template.findAll(Author.class).stream()
            .collect(Collectors.toMap(Author::getLastname, Function.identity()));
    }

    private Map<String, Genre> getGenres(MongockTemplate template) {
        return template.findAll(Genre.class).stream()
            .collect(Collectors.toMap(Genre::getName, Function.identity()));
    }

}
