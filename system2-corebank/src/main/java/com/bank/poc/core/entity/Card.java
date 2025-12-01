package com.bank.poc.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
public class Card {
    
    @Id
    private String cardNumber;
    private String pinHash;
    private double balance;
    private String customerName;
    private LocalDateTime createdAt;

    public Card() {
        this.createdAt = LocalDateTime.now();
    }

    public Card(String cardNumber, String pinHash, double balance, String customerName) {
        this.cardNumber = cardNumber;
        this.pinHash = pinHash;
        this.balance = balance;
        this.customerName = customerName;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getCardNumber() { 
        return cardNumber; 
    }
    
    public void setCardNumber(String cardNumber) { 
        this.cardNumber = cardNumber; 
    }

    public String getPinHash() { 
        return pinHash; 
    }
    
    public void setPinHash(String pinHash) { 
        this.pinHash = pinHash; 
    }

    public double getBalance() { 
        return balance; 
    }
    
    public void setBalance(double balance) { 
        this.balance = balance; 
    }

    public String getCustomerName() { 
        return customerName; 
    }
    
    public void setCustomerName(String customerName) { 
        this.customerName = customerName; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }
}