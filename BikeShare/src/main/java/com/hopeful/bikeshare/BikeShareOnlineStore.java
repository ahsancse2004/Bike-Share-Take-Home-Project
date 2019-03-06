package com.hopeful.bikeshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.hopeful.bikeshare"})
// same as @Configuration @EnableAutoConfiguration @ComponentScan combined
public class BikeShareOnlineStore {

    public static void main(String[] args) {
        SpringApplication.run(BikeShareOnlineStore.class, args);
    }
}
