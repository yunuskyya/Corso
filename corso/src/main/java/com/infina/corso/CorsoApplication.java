package com.infina.corso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.infina.corso.repository")
@EntityScan(basePackages = "com.infina.corso.model")
public class CorsoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CorsoApplication.class, args);
	}

}
