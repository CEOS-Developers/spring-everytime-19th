package com.ceos19.everytime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EverytimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EverytimeApplication.class, args);
	}

}
