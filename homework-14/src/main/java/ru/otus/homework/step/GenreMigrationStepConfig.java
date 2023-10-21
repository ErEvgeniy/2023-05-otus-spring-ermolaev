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
import ru.otus.homework.model.Genre;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class GenreMigrationStepConfig {

    private final DataSource dataSource;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public MongoItemReader<Genre> genreMongoItemReader(MongoTemplate mongoTemplate) {
        return new MongoItemReaderBuilder<Genre>()
                .name("mongoGenreReader")
                .template(mongoTemplate)
                .targetType(Genre.class)
                .jsonQuery("{}")
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Genre> genreInsertTempTable() {
        JdbcBatchItemWriter<Genre> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO temp_genre_cross_ids(id_mongo, id_postgres) " +
                "VALUES (:id, nextval('genres_genre_id_seq'))");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public JdbcBatchItemWriter<Genre> genreJdbcBatchItemWriter() {
        JdbcBatchItemWriter<Genre> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO genres(genre_id, name) " +
                "VALUES ((SELECT id_postgres FROM temp_genre_cross_ids WHERE id_mongo = :id), :name)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public CompositeItemWriter<Genre> genreCompositeItemWriter(
            JdbcBatchItemWriter<Genre> genreInsertTempTable,
            JdbcBatchItemWriter<Genre> genreJdbcBatchItemWriter
    ) {
        CompositeItemWriter<Genre> writer = new CompositeItemWriter<>();
        writer.setDelegates(List.of(genreInsertTempTable, genreJdbcBatchItemWriter));
        return writer;
    }

    @Bean
    public Step genreMigrationStep(
            MongoItemReader<Genre> genreMongoItemReader,
            CompositeItemWriter<Genre> genreCompositeItemWriter
    ) {
        return new StepBuilder("genreMigrationStep", jobRepository)
                .<Genre, Genre>chunk(10, platformTransactionManager)
                .reader(genreMongoItemReader)
                .writer(genreCompositeItemWriter)
                .allowStartIfComplete(true)
                .build();
    }
}
