package com.example.abrazado.wallet.service;

import com.example.abrazado.wallet.entity.Account;

public interface AccountService {

    public Account getAccountById(Long id);
    public Account createAccount(Account account);

}