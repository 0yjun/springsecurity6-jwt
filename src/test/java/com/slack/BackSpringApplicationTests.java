package com.slack;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BackSpringApplicationTests {

    @Test
    void contextLoads() {
        assertThat(1).isEqualTo(1);
    }

}
