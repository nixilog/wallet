package com.example.abrazado.wallet.service;

import java.util.List;

import com.example.abrazado.wallet.entity.Transaction;

public interface TransactionService {

    public Transaction getTransactionById(Long id);
    public List<Transaction> getAllTransactionsByAccountId(Long id);
    public List<Transaction> getOutgoingTransactionsByAccountId(Long id);
    public List<Transaction> getIncomingTransactionsByAccountId(Long id);
    public Transaction createTransaction(Transaction transaction);

}