package com.sks.tariff_01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching

public class Tariff01Application {

    public static void main(String[] args) {
        SpringApplication.run(Tariff01Application.class, args);
    }

}
