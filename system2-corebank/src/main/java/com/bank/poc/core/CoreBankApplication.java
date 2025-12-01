package com.bank.poc.core;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class CoreBankApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoreBankApplication.class, args);
        System.out.println("===========================================");
        System.out.println("System 2 Core Banking Started on Port 8082");
        System.out.println("H2 Console: http://localhost:8082/h2-console");
        System.out.println("===========================================");
    }
}
