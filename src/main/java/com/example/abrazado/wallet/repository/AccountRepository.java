package com.example.abrazado.wallet.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.abrazado.wallet.entity.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

}