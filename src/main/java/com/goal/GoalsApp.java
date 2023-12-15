package com.goal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(scanBasePackages = {"com.goal"})
public class GoalsApp {
    public static void main(String[] args) {
        SpringApplication.run(GoalsApp.class, args);
    }
}
