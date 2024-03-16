package com.ceos19.everytime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// Jpa의 감시 기능을 활성화하여 @CreatedDate, ModifiedDate 기능 활성화. 이 어노테이션은 Main 클래스에 사용한다
@EnableJpaAuditing
@SpringBootApplication
public class EverytimeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EverytimeApplication.class, args);
    }

}
