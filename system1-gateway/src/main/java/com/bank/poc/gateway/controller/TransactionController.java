package com.bank.poc.gateway.controller;

import com.bank.poc.gateway.dto.TransactionRequest;
import com.bank.poc.gateway.dto.TransactionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${system2.url}")
    private String system2Url;

    @PostMapping("/transaction")
    public ResponseEntity<TransactionResponse> handleTransaction(
            @RequestBody TransactionRequest request) {
        
        logger.info("Received transaction request for card: {}", 
            maskCardNumber(request.getCardNumber()));

        // Validation 1: Card Number
        if (request.getCardNumber() == null || request.getCardNumber().isEmpty()) {
            logger.warn("Transaction rejected: Card number is required");
            return ResponseEntity.badRequest()
                .body(new TransactionResponse(false, "Card number is required", null));
        }

        // Validation 2: PIN
        if (request.getPin() == null || request.getPin().isEmpty()) {
            logger.warn("Transaction rejected: PIN is required");
            return ResponseEntity.badRequest()
                .body(new TransactionResponse(false, "PIN is required", null));
        }

        // Validation 3: Amount
        if (request.getAmount() <= 0) {
            logger.warn("Transaction rejected: Amount must be positive");
            return ResponseEntity.badRequest()
                .body(new TransactionResponse(false, "Amount must be positive", null));
        }

        // Validation 4: Type
        if (request.getType() == null || 
            (!request.getType().equals("withdraw") && !request.getType().equals("topup"))) {
            logger.warn("Transaction rejected: Invalid type");
            return ResponseEntity.badRequest()
                .body(new TransactionResponse(false, 
                    "Type must be 'withdraw' or 'topup'", null));
        }

        // Routing Logic: Card range validation - only cards starting with '4'
        if (!request.getCardNumber().startsWith("4")) {
            logger.warn("Transaction rejected: Card range not supported for card: {}", 
                maskCardNumber(request.getCardNumber()));
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new TransactionResponse(false, "Card range not supported", null));
        }

        try {
            logger.info("Routing transaction to System 2: {}", system2Url);
            // Forward to System 2
            ResponseEntity<TransactionResponse> response = restTemplate.postForEntity(
                system2Url + "/api/process",
                request,
                TransactionResponse.class
            );
            
            logger.info("Transaction processed successfully");
            return response;
            
        } catch (Exception e) {
            logger.error("Error processing transaction: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new TransactionResponse(false, 
                    "Transaction processing failed: " + e.getMessage(), null));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("System 1 Gateway is running");
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "****" + cardNumber.substring(cardNumber.length() - 4);
    }
}