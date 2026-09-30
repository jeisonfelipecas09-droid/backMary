package com.proyecto.backjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BackJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackJavaApplication.class, args);
    }
}
