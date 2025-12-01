package com.bank.poc.core.controller;

import com.bank.poc.core.dto.ProcessResponse;
import com.bank.poc.core.entity.Card;
import com.bank.poc.core.entity.Transaction;
import com.bank.poc.core.service.CardService;
import com.bank.poc.core.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CardService cardService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/balance/{cardNumber}")
    public ResponseEntity<ProcessResponse> getBalance(@PathVariable String cardNumber) {
        Optional<Card> cardOpt = cardService.getCard(cardNumber);
        if (cardOpt.isEmpty()) {
            return ResponseEntity.ok(
                new ProcessResponse(false, "Card not found", null)
            );
        }

        Card card = cardOpt.get();
        Map<String, Object> data = new HashMap<>();
        data.put("cardNumber", maskCardNumber(card.getCardNumber()));
        data.put("balance", card.getBalance());
        data.put("customerName", card.getCustomerName());

        return ResponseEntity.ok(
            new ProcessResponse(true, "Balance retrieved", data)
        );
    }

    @GetMapping("/transactions/{cardNumber}")
    public ResponseEntity<ProcessResponse> getTransactions(@PathVariable String cardNumber) {
        List<Transaction> transactions = transactionService.getTransactionsByCard(cardNumber);
        
        return ResponseEntity.ok(
            new ProcessResponse(true, "Transactions retrieved", transactions)
        );
    }

    @GetMapping("/transactions/all")
    public ResponseEntity<ProcessResponse> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        
        return ResponseEntity.ok(
            new ProcessResponse(true, "All transactions retrieved", transactions)
        );
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "****" + cardNumber.substring(cardNumber.length() - 4);
    }
}