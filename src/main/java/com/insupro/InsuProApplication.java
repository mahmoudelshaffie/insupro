package com.insupro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.insupro.*") 
@EntityScan("com.insupro.*")
@SpringBootApplication
public class InsuProApplication {

	public static void main(String[] args) {
		SpringApplication.run(InsuProApplication.class, args);
	}
	
}
