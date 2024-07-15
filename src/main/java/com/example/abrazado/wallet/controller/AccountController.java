package com.example.abrazado.wallet.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.abrazado.wallet.entity.Account;
import com.example.abrazado.wallet.service.AccountService;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    // Endpoint to get an account using the account id
    @GetMapping("/account/{id}")
	public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
		Account account = accountService.getAccountById(id);
        return new ResponseEntity<>(account, HttpStatus.OK);
	}

    // Endpoint to get an account's balance using the account id
    @GetMapping("/account/{id}/balance")
    public ResponseEntity<BigDecimal> getAccountBalanceById(@PathVariable Long id) {
		Account account = accountService.getAccountById(id);
        return new ResponseEntity<>(account.getBalance(), HttpStatus.OK);
    }

    // Endpoint to create an account
    @PostMapping("/account")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return new ResponseEntity<>(accountService.createAccount(account), HttpStatus.OK);
	}

}
