package com.bank.poc.gateway.dto;

public class TransactionRequest {
    private String cardNumber;
    private String pin;
    private double amount;
    private String type;

    // Constructors
    public TransactionRequest() {}

    public TransactionRequest(String cardNumber, String pin, double amount, String type) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.amount = amount;
        this.type = type;
    }

    // Getters and Setters
    public String getCardNumber() { 
        return cardNumber; 
    }
    
    public void setCardNumber(String cardNumber) { 
        this.cardNumber = cardNumber; 
    }

    public String getPin() { 
        return pin; 
    }
    
    public void setPin(String pin) { 
        this.pin = pin; 
    }

    public double getAmount() { 
        return amount; 
    }
    
    public void setAmount(double amount) { 
        this.amount = amount; 
    }

    public String getType() { 
        return type; 
    }
    
    public void setType(String type) { 
        this.type = type; 
    }

    @Override
    public String toString() {
        return "TransactionRequest{" +
                "cardNumber='****" + (cardNumber != null && cardNumber.length() > 4 ? 
                    cardNumber.substring(cardNumber.length() - 4) : "") + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                '}';
    }
}