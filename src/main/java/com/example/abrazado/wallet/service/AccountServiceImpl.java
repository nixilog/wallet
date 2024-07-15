package com.example.abrazado.wallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.abrazado.wallet.entity.Account;
import com.example.abrazado.wallet.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // Method to get an account by id
    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).get();
    }

    // Method to create an account
    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

}