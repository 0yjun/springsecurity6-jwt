package com.security;

import com.security.domain.sample.repository.SampleRepository;
import com.security.domain.sample.repository.entry.Sample;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackSpringApplication.class, args);
    }

}
