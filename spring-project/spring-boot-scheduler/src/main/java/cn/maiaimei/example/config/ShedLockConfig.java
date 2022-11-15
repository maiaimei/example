package cn.maiaimei.example.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.ScheduledLockConfiguration;
import net.javacrumbs.shedlock.spring.ScheduledLockConfigurationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.TimeZone;

@Configuration
public class ShedLockConfig {

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(
                JdbcTemplateLockProvider.Configuration.builder()
                        .withJdbcTemplate(new JdbcTemplate(dataSource))
                        .withTimeZone(TimeZone.getTimeZone("UTC"))
                        .build()
        );
    }

    @Bean
    public ScheduledLockConfiguration scheduledLockConfiguration(LockProvider lockProvider) {
        return ScheduledLockConfigurationBuilder.withLockProvider(lockProvider)
                .withPoolSize(10)
                .withDefaultLockAtMostFor(Duration.ofMinutes(10))
                .build();
    }

}
