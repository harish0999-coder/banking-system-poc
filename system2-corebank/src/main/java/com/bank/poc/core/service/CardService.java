package com.bank.poc.core.service;

import com.bank.poc.core.entity.Card;
import com.bank.poc.core.repository.CardRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    public String hashPin(String pin) {
        // SHA-256 hashing - NEVER store plain text PINs
        return DigestUtils.sha256Hex(pin);
    }

    public boolean validatePin(String cardNumber, String inputPin) {
        Optional<Card> cardOpt = cardRepository.findByCardNumber(cardNumber);
        if (cardOpt.isEmpty()) {
            return false;
        }
        
        Card card = cardOpt.get();
        String inputPinHash = hashPin(inputPin);
        return card.getPinHash().equals(inputPinHash);
    }

    public Optional<Card> getCard(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber);
    }

    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }

    public boolean updateBalance(String cardNumber, double newBalance) {
        Optional<Card> cardOpt = cardRepository.findByCardNumber(cardNumber);
        if (cardOpt.isEmpty()) {
            return false;
        }
        
        Card card = cardOpt.get();
        card.setBalance(newBalance);
        cardRepository.save(card);
        return true;
    }
}