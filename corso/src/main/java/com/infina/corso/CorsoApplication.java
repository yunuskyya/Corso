package com.infina.corso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.infina.corso.repository")
@EntityScan(basePackages = "com.infina.corso.model")
@EnableScheduling
public class CorsoApplication {

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        SpringApplication.run(CorsoApplication.class, args);
    }
}