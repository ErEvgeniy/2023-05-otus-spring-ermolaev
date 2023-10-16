package ru.otus.homework.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LatencyHealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Health health() {
        long start = System.currentTimeMillis();

        try {
            jdbcTemplate.execute("SELECT 1");
            long latency = System.currentTimeMillis() - start;
            if (latency > 1000) {
                return Health.down()
                        .status(Status.DOWN)
                        .withDetail("message", "High latency for access to database")
                        .withDetail("latency", latency)
                        .build();
            } else {
                return Health.up()
                        .status(Status.UP)
                        .withDetail("message", "Access database latency is ok")
                        .withDetail("latency", latency)
                        .build();
            }
        } catch (DataAccessException ex) {
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", "Database is not available")
                    .build();
        }
    }

}
