package ru.otus.homework.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@RequiredArgsConstructor
@ShellComponent
public class BatchCommands {

    private final Job migrationJob;

    private final JobLauncher jobLauncher;

    @ShellMethod(value = "startMigrationJob", key = "sm")
    public void startMigrationJobWithJobLauncher() throws Exception {
        jobLauncher.run(migrationJob, new JobParameters());
    }

}
