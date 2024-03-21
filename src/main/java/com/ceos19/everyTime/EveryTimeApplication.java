package com.ceos19.everyTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EveryTimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EveryTimeApplication.class, args);
	}

}
