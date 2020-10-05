package com.syx.raffle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RaffleApplication {

    public static void main(String[] args) {
        SpringApplication.run(RaffleApplication.class, args);
    }

}
