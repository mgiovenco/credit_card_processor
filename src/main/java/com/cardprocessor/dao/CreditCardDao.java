package com.cardprocessor.dao;


import com.cardprocessor.domain.CreditCard;
import com.cardprocessor.domain.CreditCardTransaction;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class CreditCardDao {

    // Both lists represented the database tables for this code example.
    private final Set<CreditCard> creditCards;
    private final Set<CreditCardTransaction> creditCardTransactions;

    public CreditCardDao() {
        this.creditCards = new HashSet<>();
        this.creditCardTransactions = new HashSet<>();
    }

    public void addCreditCard(CreditCard creditCard) {
        creditCards.add(creditCard);
    }

    public void addCreditCardTransation(CreditCardTransaction creditCardTransaction, CreditCard creditCard) {
        creditCardTransactions.add(creditCardTransaction);
        creditCard.setBalance(creditCard.getBalance().add(creditCardTransaction.getAmount()));
    }

    public Set<CreditCard> getAllCreditCards() {
        return creditCards;
    }

    public CreditCard getCreditCardByName(String name) {
        // Note: This would be optimized in database through index or lookup table.
        Set<CreditCard> allCreditCards = getAllCreditCards();

        for(CreditCard creditCard : allCreditCards) {
            if(creditCard.getName().equals(name)){
                return creditCard;
            }
        }
        return null;
    }

}
