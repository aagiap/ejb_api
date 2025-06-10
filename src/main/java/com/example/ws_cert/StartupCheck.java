package com.example.ws_cert;

import com.example.ws_cert.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupCheck implements CommandLineRunner {
    private final UserRepository userRepository;

    public StartupCheck(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        System.out.println("✅ UserRepository injected: " + userRepository);
    }
}
