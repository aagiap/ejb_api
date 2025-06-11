package com.example.ws_cert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.ws_cert.repository")
public class WsCertApplication {

    public static void main(String[] args) {
        SpringApplication.run(WsCertApplication.class, args);
    }

}
