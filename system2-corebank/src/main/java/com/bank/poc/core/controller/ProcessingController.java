package com.bank.poc.core.controller;

import com.bank.poc.core.dto.ProcessRequest;
import com.bank.poc.core.dto.ProcessResponse;
import com.bank.poc.core.entity.Card;
import com.bank.poc.core.entity.Transaction;
import com.bank.poc.core.service.CardService;
import com.bank.poc.core.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ProcessingController {

    private static final Logger logger = LoggerFactory.getLogger(ProcessingController.class);

    @Autowired
    private CardService cardService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/process")
    public ResponseEntity<ProcessResponse> processTransaction(
            @RequestBody ProcessRequest request) {
        
        logger.info("Processing transaction for card: {}", 
            maskCardNumber(request.getCardNumber()));

        // Step 1: Find card
        Optional<Card> cardOpt = cardService.getCard(request.getCardNumber());
        if (cardOpt.isEmpty()) {
            Transaction failedTx = new Transaction(
                request.getCardNumber(),
                request.getType(),
                request.getAmount(),
                "FAILED",
                "Invalid card"
            );
            transactionService.saveTransaction(failedTx);
            
            logger.warn("Transaction failed: Invalid card");
            return ResponseEntity.ok(
                new ProcessResponse(false, "Invalid card", null)
            );
        }

        Card card = cardOpt.get();

        // Step 2: Validate PIN
        if (!cardService.validatePin(request.getCardNumber(), request.getPin())) {
            Transaction failedTx = new Transaction(
                request.getCardNumber(),
                request.getType(),
                request.getAmount(),
                "FAILED",
                "Invalid PIN"
            );
            transactionService.saveTransaction(failedTx);
            
            logger.warn("Transaction failed: Invalid PIN");
            return ResponseEntity.ok(
                new ProcessResponse(false, "Invalid PIN", null)
            );
        }

        // Step 3: Process based on type
        if (request.getType().equals("withdraw")) {
            return processWithdrawal(card, request);
        } else if (request.getType().equals("topup")) {
            return processTopup(card, request);
        }

        return ResponseEntity.badRequest()
            .body(new ProcessResponse(false, "Invalid transaction type", null));
    }

    private ResponseEntity<ProcessResponse> processWithdrawal(Card card, ProcessRequest request) {
        // Check balance
        if (card.getBalance() < request.getAmount()) {
            Transaction failedTx = new Transaction(
                request.getCardNumber(),
                request.getType(),
                request.getAmount(),
                "FAILED",
                "Insufficient balance"
            );
            transactionService.saveTransaction(failedTx);
            
            logger.warn("Withdrawal failed: Insufficient balance");
            return ResponseEntity.ok(
                new ProcessResponse(false, "Insufficient balance", null)
            );
        }

        // Update balance
        double newBalance = card.getBalance() - request.getAmount();
        cardService.updateBalance(card.getCardNumber(), newBalance);

        // Save successful transaction
        Transaction successTx = new Transaction(
            request.getCardNumber(),
            request.getType(),
            request.getAmount(),
            "SUCCESS",
            "Withdrawal successful"
        );
        transactionService.saveTransaction(successTx);

        logger.info("Withdrawal successful. New balance: {}", newBalance);
        return ResponseEntity.ok(
            new ProcessResponse(true, "Withdrawal successful", 
                new ProcessResponse.TransactionData(successTx.getId(), newBalance))
        );
    }

    private ResponseEntity<ProcessResponse> processTopup(Card card, ProcessRequest request) {
        // Update balance
        double newBalance = card.getBalance() + request.getAmount();
        cardService.updateBalance(card.getCardNumber(), newBalance);

        // Save successful transaction
        Transaction successTx = new Transaction(
            request.getCardNumber(),
            request.getType(),
            request.getAmount(),
            "SUCCESS",
            "Top-up successful"
        );
        transactionService.saveTransaction(successTx);

        logger.info("Top-up successful. New balance: {}", newBalance);
        return ResponseEntity.ok(
            new ProcessResponse(true, "Top-up successful", 
                new ProcessResponse.TransactionData(successTx.getId(), newBalance))
        );
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("System 2 Core Banking is running");
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "****" + cardNumber.substring(cardNumber.length() - 4);
    }
}