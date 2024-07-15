package com.example.abrazado.wallet.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.example.abrazado.wallet.entity.Account;
import com.example.abrazado.wallet.entity.Transaction;
import com.example.abrazado.wallet.repository.AccountRepository;
import com.example.abrazado.wallet.repository.TransactionRepository;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    // Method to get a transaction by id
    @Override
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).get();
    }

    // Method to get all transactions related to an account
    @Override
    public List<Transaction> getAllTransactionsByAccountId(Long id) {
        return transactionRepository.getAllTransactionsByAccountId(id);
    }

    // Method to get all outgoing transactions from an account
    @Override
    public List<Transaction> getOutgoingTransactionsByAccountId(Long id) {
        return transactionRepository.getOutgoingTransactionsByAccountId(id);
    }

    // Method to get all incoming transactions from an account
    @Override
    public List<Transaction> getIncomingTransactionsByAccountId(Long id) {
        return transactionRepository.getIncomingTransactionsByAccountId(id);
    }

    // Method to create a transaction
    // In case of non-existent source or destination, this method will also create accounts accordingly
    // Implement retryable with a random jitter delay and optimistic locking for concurrency
    // 2-5 seconds seem to work fine for 100 threads on current development machine; results may vary with hardware
    @Retryable(value = OptimisticLockingFailureException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2.5, random = true))
    @Transactional(TxType.REQUIRES_NEW)
    @Override
    public Transaction createTransaction(Transaction transaction) {
        // fetch the source and destination if existing, otherwise create them
        Account source = fetchOrCreateSource(transaction);
        Account destination = fetchOrCreateDestination(transaction);

        // save and flush the accounts first so we can get the generated ids for newly created accounts...
        List<Account> accounts = (List<Account>) accountRepository.saveAll(List.of(source, destination));

        // ...and add them to the transaction record
        if (source.getId() != null) {
            transaction.setSourceId(accounts.get(0).getId());
        }
        if (destination.getId() != null) {
            transaction.setDestinationId(accounts.get(1).getId());
        }
        // finally, save and flush the transaction
        return transactionRepository.save(transaction);
    }

    private Account fetchOrCreateSource(Transaction transaction) {
        Account source = null;
        // if source account id is provided, attempt to retrieve it
        if (transaction.getSourceId() != null) {
            Optional<Account> sourceOptional = accountRepository.findById(transaction.getSourceId());
            // if found, update the balance
            if (sourceOptional.isPresent()) {
                source = sourceOptional.get();
                BigDecimal currentSourceBalance = source.getBalance();
                source.setBalance(currentSourceBalance.subtract(transaction.getAmount()));
            } 
        }

        // if source is still null at this point, either source account id was not provided or it is non-existent
        // create a new account for the source with starting balance as zero - transaction amount
        if (source == null) {
            source = new Account(transaction.getSource(),
                BigDecimal.ZERO.subtract(transaction.getAmount()));
        }
        return source;
    }

    private Account fetchOrCreateDestination(Transaction transaction) {
        Account destination = null;
        // if destination account id is provided, attempt to retrieve it
        if (transaction.getDestinationId() != null) {
            Optional<Account> destinationOptional = accountRepository.findById(transaction.getDestinationId());
            if (destinationOptional.isPresent()) {
                destination = destinationOptional.get();
                BigDecimal currentDestinationBalance = destination.getBalance();
                destination.setBalance(currentDestinationBalance.add(transaction.getAmount()));
            }
        }

        // if destination is still null at this point, either destination account id was not provided or it is non-existent
        // create a new account for the destination with starting balance as zero + transaction amount
        if (destination == null) {
            destination = new Account(transaction.getDestination(),
                BigDecimal.ZERO.add(transaction.getAmount()));
        }
        return destination;
    }
}