package com.bank.poc.core.service;

import com.bank.poc.core.entity.Transaction;
import com.bank.poc.core.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByCard(String cardNumber) {
        return transactionRepository.findByCardNumberOrderByTimestampDesc(cardNumber);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAllByOrderByTimestampDesc();
    }
}