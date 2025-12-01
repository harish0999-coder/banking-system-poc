package com.bank.poc.gateway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        System.out.println("===========================================");
        System.out.println("System 1 Gateway Started on Port 8081");
        System.out.println("===========================================");
    }
}
