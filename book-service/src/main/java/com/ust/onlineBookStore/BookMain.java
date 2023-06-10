package com.ust.onlineBookStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BookMain {
    public static void main(String[] args) {
        SpringApplication.run(BookMain.class,args);
    }
}