package com.bank.poc.core.config;

import com.bank.poc.core.entity.Card;
import com.bank.poc.core.repository.CardRepository;
import com.bank.poc.core.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardService cardService;

    @Override
    public void run(String... args) throws Exception {
        // Initialize test cards - only cards starting with '4' are supported
        
        Card card1 = new Card();
        card1.setCardNumber("4123456789012345");
        card1.setPinHash(cardService.hashPin("1234"));
        card1.setBalance(1000.00);
        card1.setCustomerName("John Doe");
        cardRepository.save(card1);

        Card card2 = new Card();
        card2.setCardNumber("4987654321098765");
        card2.setPinHash(cardService.hashPin("5678"));
        card2.setBalance(2500.00);
        card2.setCustomerName("Jane Smith");
        cardRepository.save(card2);

        // This card will be rejected by System 1 (doesn't start with 4)
        Card card3 = new Card();
        card3.setCardNumber("5111222233334444");
        card3.setPinHash(cardService.hashPin("9999"));
        card3.setBalance(500.00);
        card3.setCustomerName("Test User");
        cardRepository.save(card3);

        System.out.println("===========================================");
        System.out.println("Test Cards Initialized:");
        System.out.println("Card 1: 4123456789012345 | PIN: 1234 | Balance: $1000.00 | Customer: John Doe");
        System.out.println("Card 2: 4987654321098765 | PIN: 5678 | Balance: $2500.00 | Customer: Jane Smith");
        System.out.println("Card 3: 5111222233334444 | PIN: 9999 | Balance: $500.00 | Customer: Test User (Will be rejected by System 1)");
        System.out.println("===========================================");
    }
}