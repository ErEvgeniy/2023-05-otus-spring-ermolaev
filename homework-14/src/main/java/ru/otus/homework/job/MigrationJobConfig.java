package ru.otus.homework.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class MigrationJobConfig {

    private final DataSource dataSource;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job migrationJob(Step authorMigrationStep, Step genreMigrationStep, Step bookMigrationStep) {
        return new JobBuilder("migrationJob", jobRepository)
                .start(createTempAuthorCrossIdTable(jobRepository))
                .next(createTempGenreCrossIdTable(jobRepository))
                .next(createTempBookCrossIdTable(jobRepository))
                .next(authorMigrationStep)
                .next(genreMigrationStep)
                .next(bookMigrationStep)
                .next(dropTempAuthorCrossIdTable(jobRepository))
                .next(dropTempGenreCrossIdTable(jobRepository))
                .next(dropTempBookCrossIdTable(jobRepository))
                .build();
    }

    @Bean
    public TaskletStep createTempAuthorCrossIdTable(JobRepository jobRepository) {
        return new StepBuilder("createTempAuthorCrossIdTable", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(
                            "CREATE TABLE temp_author_cross_ids" +
                                    " (id_mongo VARCHAR(255) NOT NULL, id_postgres INT NOT NULL)"
                    );
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public TaskletStep createTempGenreCrossIdTable(JobRepository jobRepository) {
        return new StepBuilder("createTempGenreCrossIdTable", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(
                            "CREATE TABLE temp_genre_cross_ids" +
                                    " (id_mongo VARCHAR(255) NOT NULL, id_postgres INT NOT NULL)"
                    );
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public TaskletStep createTempBookCrossIdTable(JobRepository jobRepository) {
        return new StepBuilder("createTempBookCrossIdTable", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(
                            "CREATE TABLE temp_book_cross_ids" +
                                    " (id_mongo VARCHAR(255) NOT NULL, id_postgres INT NOT NULL)"
                    );
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public TaskletStep dropTempAuthorCrossIdTable(JobRepository jobRepository) {
        return new StepBuilder("dropTempCrossIdTable", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute("DROP TABLE temp_author_cross_ids");
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public TaskletStep dropTempGenreCrossIdTable(JobRepository jobRepository) {
        return new StepBuilder("dropTempCrossIdTable", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute("DROP TABLE temp_genre_cross_ids");
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public TaskletStep dropTempBookCrossIdTable(JobRepository jobRepository) {
        return new StepBuilder("dropTempCrossIdTable", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute("DROP TABLE temp_book_cross_ids");
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

}
