package madstp.backend.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.FileCopyUtils;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Configuration
public class DataInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            try {
                // Load the SQL file from resources
                ClassPathResource resource = new ClassPathResource("data.sql");
                String sql = FileCopyUtils.copyToString(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
                );

                // Split the SQL file into individual statements
                String[] statements = sql.split(";");

                // Execute each statement
                for (String statement : statements) {
                    if (!statement.trim().isEmpty()) {
                        jdbcTemplate.execute(statement);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Error initializing data: " + e.getMessage(), e);
            }
        };
    }
}
