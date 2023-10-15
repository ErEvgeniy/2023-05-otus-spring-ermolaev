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
                .start(createTempCrossIdTable(jobRepository))
                .next(authorMigrationStep)
                .next(genreMigrationStep)
                .next(bookMigrationStep)
                .next(dropTempCrossIdTable(jobRepository))
                .build();
    }

    @Bean
    public TaskletStep createTempCrossIdTable(JobRepository jobRepository) {
        return new StepBuilder("createTempCrossIdTable", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(
                            "CREATE TABLE temp_cross_ids (id_mongo VARCHAR(255) NOT NULL, id_postgres INT NOT NULL)"
                    );
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public TaskletStep dropTempCrossIdTable(JobRepository jobRepository) {
        return new StepBuilder("dropTempCrossIdTable", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute("DROP TABLE temp_cross_ids");
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

}
