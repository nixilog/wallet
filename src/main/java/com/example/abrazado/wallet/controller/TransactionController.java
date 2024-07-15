package com.example.abrazado.wallet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.abrazado.wallet.entity.Transaction;
import com.example.abrazado.wallet.service.TransactionService;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

	// Endpoint to get all transactions related to a given account using the account id
    @GetMapping("/account/{id}/transactions")
	public ResponseEntity<List<Transaction>> getAllTransactionsByAccountId(@PathVariable Long id) {
		List<Transaction> transactions = transactionService.getAllTransactionsByAccountId(id);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
	}

	// Endpoint to get all outgoing transactions from a given account using the account id
    @GetMapping("/account/{id}/transactions/out")
	public ResponseEntity<List<Transaction>> getOutgoingTransactionsByAccountId(@PathVariable Long id) {
		List<Transaction> transactions = transactionService.getOutgoingTransactionsByAccountId(id);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
	}

	// Endpoint to get all incoming transactions into a given account using the account id
    @GetMapping("/account/{id}/transactions/in")
	public ResponseEntity<List<Transaction>> getIncomingTransactionsByAccountId(@PathVariable Long id) {
		List<Transaction> transactions = transactionService.getIncomingTransactionsByAccountId(id);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
	}

	// Endpoint to create a transaction
	@PostMapping("/transaction")
	public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
		return new ResponseEntity<>(transactionService.createTransaction(transaction), HttpStatus.OK);
	}
}
