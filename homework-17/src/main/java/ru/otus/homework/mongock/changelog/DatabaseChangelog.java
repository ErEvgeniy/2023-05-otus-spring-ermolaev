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
import ru.otus.homework.repository.CommentRepository;
import ru.otus.homework.repository.GenreRepository;

import java.util.ArrayList;
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

    private static final String PLAY = "PLAY";

    @ChangeSet(order = "001", id = "dropDb", author = "eermolaev", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "eermolaev")
    public void insertAuthors(AuthorRepository authorRepository) {
        authorRepository.insert(List.of(
            new Author(null, "ALEKSANDR", "SERGEEVICH", PUSHKIN),
            new Author(null, "NIKOLAY", "VASILEVICH", GOGOL),
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
            new Genre(null, STORY),
            new Genre(null, PLAY),
            new Genre(null, NOVEL)
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
            new Book(null, ONEGIN, genres.get(NOVEL), authors.get(PUSHKIN), null),
            new Book(null, QUEEN_OF_SPADES, genres.get(STORY), authors.get(PUSHKIN), null),
            new Book(null, VIY, genres.get(STORY), authors.get(GOGOL), null),
            new Book(null, SHINEL, genres.get(STORY), authors.get(GOGOL), null),
            new Book(null, THE_GOVERNMENT_INSPECTOR, genres.get(PLAY), authors.get(GOGOL), null)
        ));
    }

    @ChangeSet(order = "006", id = "insertComments", author = "eermolaev")
    public void insertComments(
        BookRepository bookRepository,
        CommentRepository commentRepository
    ) {
        Map<String, Book> books = getBooks(bookRepository);

        commentRepository.insert(List.of(
            new Comment(null, "COMMENTARY 1 FOR: EVGENIY ONEGIN", books.get(ONEGIN)),
            new Comment(null, "COMMENTARY 2 FOR: EVGENIY ONEGIN", books.get(ONEGIN)),
            new Comment(null, "COMMENTARY 3 FOR: EVGENIY ONEGIN", books.get(ONEGIN)),
            new Comment(null, "COMMENTARY FOR: QUEEN OF SPADES", books.get(QUEEN_OF_SPADES)),
            new Comment(null, "COMMENTARY FOR: VIY", books.get(VIY)),
            new Comment(null, "COMMENTARY FOR: SHINEL", books.get(SHINEL)),
            new Comment(null, "COMMENTARY FOR: THE GOVERNMENT INSPECTOR", books.get(THE_GOVERNMENT_INSPECTOR))
        ));
    }

    @ChangeSet(order = "007", id = "linkCommentsWithBooks", author = "eermolaev")
    public void linkCommentsWithBooks(
        BookRepository bookRepository,
        CommentRepository commentRepository
    ) {
        Map<String, Book> books = getBooks(bookRepository);
        List<Comment> comments = commentRepository.findAll();

        comments.forEach(comment -> {
            Book book = books.get(comment.getBook().getName());
            if (book.getComments() == null) {
                List<Comment> commentList = new ArrayList<>();
                commentList.add(comment);
                book.setComments(commentList);
            } else {
                book.getComments().add(comment);
            }
        });

        bookRepository.saveAll(books.values());
    }

    private Map<String, Author> getAuthors(AuthorRepository authorRepository) {
        return authorRepository.findAll().stream()
            .collect(Collectors.toMap(Author::getLastname, Function.identity()));
    }

    private Map<String, Genre> getGenres(GenreRepository genreRepository) {
        return genreRepository.findAll().stream()
            .collect(Collectors.toMap(Genre::getName, Function.identity()));
    }

    private Map<String, Book> getBooks(BookRepository bookRepository) {
        return bookRepository.findAll().stream()
            .collect(Collectors.toMap(Book::getName, Function.identity()));
    }

}
