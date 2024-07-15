package com.example.abrazado.wallet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.abrazado.wallet.entity.Account;
import com.example.abrazado.wallet.entity.Transaction;
import com.example.abrazado.wallet.repository.AccountRepository;
import com.example.abrazado.wallet.service.TransactionService;

@SpringBootTest
class LockingTests {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void shouldUpdateBalance_withOptimisticLocking() throws InterruptedException {
        // given
        final int threadCount = 100;
        final Account source = accountRepository.save(new Account("lock_test_source", BigDecimal.ZERO));
        final Account destination = accountRepository.save(new Account("lock_test_destination", BigDecimal.ZERO));
        assertEquals(0, source.getVersion());
        assertEquals(0, destination.getVersion());

        // when
        final ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> transactionService.createTransaction(new Transaction(source.getId(),
                "lock_test_source",
                destination.getId(),
                "lock_test_destination",
                BigDecimal.valueOf(1000),
                LocalDateTime.now())));
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        // then
        final Account updatedSource = accountRepository.findById(source.getId()).get();

        assertAll(
                () -> assertEquals(100, updatedSource.getVersion()),
                () -> assertEquals(0, BigDecimal.valueOf(-100000).compareTo(updatedSource.getBalance()))
        );
    }
}