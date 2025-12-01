package com.bank.poc.core.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String cardNumber;
    private String type;
    private double amount;
    private LocalDateTime timestamp;
    private String status;
    private String reason;

    public Transaction() {
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(String cardNumber, String type, double amount, 
                      String status, String reason) {
        this.cardNumber = cardNumber;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getCardNumber() { 
        return cardNumber; 
    }
    
    public void setCardNumber(String cardNumber) { 
        this.cardNumber = cardNumber; 
    }

    public String getType() { 
        return type; 
    }
    
    public void setType(String type) { 
        this.type = type; 
    }

    public double getAmount() { 
        return amount; 
    }
    
    public void setAmount(double amount) { 
        this.amount = amount; 
    }

    public LocalDateTime getTimestamp() { 
        return timestamp; 
    }
    
    public void setTimestamp(LocalDateTime timestamp) { 
        this.timestamp = timestamp; 
    }

    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }

    public String getReason() { 
        return reason; 
    }
    
    public void setReason(String reason) { 
        this.reason = reason; 
    }
}