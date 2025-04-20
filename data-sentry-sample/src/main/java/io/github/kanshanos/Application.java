package io.github.kanshanos;

import io.github.kanshanos.datasentry.output.ContextOutput;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Hello world!
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ContextOutput contextOutput() {
        return new ConsoleOutput();
    }
}
