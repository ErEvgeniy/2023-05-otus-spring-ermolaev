package ru.otus.homework.step;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.homework.model.Book;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BookMigrationStepConfig {

    private final DataSource dataSource;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    @Bean
    public MongoItemReader<Book> bookMongoItemReader(MongoTemplate mongoTemplate) {
        return new MongoItemReaderBuilder<Book>()
                .name("mongoBookReader")
                .template(mongoTemplate)
                .targetType(Book.class)
                .jsonQuery("{}")
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Book> bookInsertTempTable() {
        JdbcBatchItemWriter<Book> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO temp_book_cross_ids(id_mongo, id_postgres) " +
                "VALUES (:id, nextval('books_book_id_seq'))");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public JdbcBatchItemWriter<Book> bookJdbcBatchItemWriter() {
        JdbcBatchItemWriter<Book> writer = new JdbcBatchItemWriter<>();
        writer.setItemPreparedStatementSetter((book, statement) -> {
            statement.setString(1, book.getName());
            statement.setString(2, book.getId());
            statement.setString(3, book.getGenre().getId());
            statement.setString(4, book.getAuthor().getId());
        });
        writer.setSql("INSERT INTO books(name, book_id, genre_id, author_id) " +
                "VALUES (?, " +
                "(SELECT id_postgres FROM temp_book_cross_ids WHERE id_mongo = ?), " +
                "(SELECT id_postgres FROM temp_genre_cross_ids WHERE id_mongo = ?), " +
                "(SELECT id_postgres FROM temp_author_cross_ids WHERE id_mongo = ?))");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public CompositeItemWriter<Book> bookCompositeItemWriter(
            JdbcBatchItemWriter<Book> bookInsertTempTable,
            JdbcBatchItemWriter<Book> bookJdbcBatchItemWriter
    ) {
        CompositeItemWriter<Book> writer = new CompositeItemWriter<>();
        writer.setDelegates(List.of(bookInsertTempTable, bookJdbcBatchItemWriter));
        return writer;
    }

    @Bean
    public Step bookMigrationStep(
            MongoItemReader<Book> bookMongoItemReader,
            CompositeItemWriter<Book> bookCompositeItemWriter
    ) {
        return new StepBuilder("bookMigrationStep", jobRepository)
                .<Book, Book>chunk(10, transactionManager)
                .reader(bookMongoItemReader)
                .writer(bookCompositeItemWriter)
                .allowStartIfComplete(true)
                .build();
    }

}
