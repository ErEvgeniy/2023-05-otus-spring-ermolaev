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
import ru.otus.homework.model.Author;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthorMigrationStepConfig {

    private final DataSource dataSource;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public MongoItemReader<Author> mongoAuthorReader(MongoTemplate mongoTemplate) {
        return new MongoItemReaderBuilder<Author>()
                .name("mongoAuthorReader")
                .template(mongoTemplate)
                .targetType(Author.class)
                .jsonQuery("{}")
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Author> authorInsertTempTable() {
        JdbcBatchItemWriter<Author> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO temp_cross_ids(id_mongo, id_postgres) " +
                "VALUES (:id, nextval('authors_author_id_seq'))");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public JdbcBatchItemWriter<Author> authorJdbcBatchItemWriter() {
        JdbcBatchItemWriter<Author> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO authors(author_id, firstname, patronymic, lastname) " +
                "VALUES ((SELECT id_postgres FROM temp_cross_ids WHERE id_mongo = :id), " +
                ":firstname, :patronymic, :lastname)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public CompositeItemWriter<Author> authorCompositeItemWriter(
            JdbcBatchItemWriter<Author> authorInsertTempTable,
            JdbcBatchItemWriter<Author> authorJdbcBatchItemWriter
    ) {
        CompositeItemWriter<Author> writer = new CompositeItemWriter<>();
        writer.setDelegates(List.of(authorInsertTempTable, authorJdbcBatchItemWriter));
        return writer;
    }

    @Bean
    public Step authorMigrationStep(
            MongoItemReader<Author> mongoAuthorReader,
            CompositeItemWriter<Author> authorCompositeItemWriter
    ) {
        return new StepBuilder("authorMigrationStep", jobRepository)
                .<Author, Author> chunk(10, platformTransactionManager)
                .reader(mongoAuthorReader)
                .writer(authorCompositeItemWriter)
                .allowStartIfComplete(true)
                .build();
    }

}
