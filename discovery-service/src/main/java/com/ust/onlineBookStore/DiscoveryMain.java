package com.ust.onlineBookStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryMain {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryMain.class,args);
    }
}