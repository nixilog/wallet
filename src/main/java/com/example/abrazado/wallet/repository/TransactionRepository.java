package com.example.abrazado.wallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.abrazado.wallet.entity.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    @Query("select t from Transaction t where t.sourceId = ?1 or t.destinationId = ?1")
    List<Transaction> getAllTransactionsByAccountId(Long id);

    @Query("select t from Transaction t where t.sourceId = ?1")
    List<Transaction> getOutgoingTransactionsByAccountId(Long id);

    @Query("select t from Transaction t where t.destinationId = ?1")
    List<Transaction> getIncomingTransactionsByAccountId(Long id);
    
}