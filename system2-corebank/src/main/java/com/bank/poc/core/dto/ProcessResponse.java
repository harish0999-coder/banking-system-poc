package com.bank.poc.core.dto;

import java.time.LocalDateTime;

public class ProcessResponse {
    private boolean success;
    private String message;
    private Object data;
    private LocalDateTime timestamp;

    public ProcessResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ProcessResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public boolean isSuccess() { 
        return success; 
    }
    
    public void setSuccess(boolean success) { 
        this.success = success; 
    }

    public String getMessage() { 
        return message; 
    }
    
    public void setMessage(String message) { 
        this.message = message; 
    }

    public Object getData() { 
        return data; 
    }
    
    public void setData(Object data) { 
        this.data = data; 
    }

    public LocalDateTime getTimestamp() { 
        return timestamp; 
    }
    
    public void setTimestamp(LocalDateTime timestamp) { 
        this.timestamp = timestamp; 
    }

    // Inner class for transaction data
    public static class TransactionData {
        private Long transactionId;
        private double newBalance;

        public TransactionData() {}

        public TransactionData(Long transactionId, double newBalance) {
            this.transactionId = transactionId;
            this.newBalance = newBalance;
        }

        public Long getTransactionId() { 
            return transactionId; 
        }
        
        public void setTransactionId(Long transactionId) { 
            this.transactionId = transactionId; 
        }

        public double getNewBalance() { 
            return newBalance; 
        }
        
        public void setNewBalance(double newBalance) { 
            this.newBalance = newBalance; 
        }
    }
}