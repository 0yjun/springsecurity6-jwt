package com.security;

import com.security.domain.sample.repository.SampleRepository;
import com.security.domain.sample.repository.entry.Sample;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackSpringApplication {

    private final SampleRepository sampleRepository;

    public BackSpringApplication(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }


    @PostConstruct
    public void init() {
        for (int i = 0; i < 10; i++) {
            Sample sample = Sample.builder()
                    .name("name"+i)
                    .build();
            sampleRepository.save(sample);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(BackSpringApplication.class, args);
    }

}
