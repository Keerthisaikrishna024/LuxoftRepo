package com.dws.challenge;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.InsufficientBalanceException;
import com.dws.challenge.repository.AccountsRepository;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ChallengeApplicationTests {

@Mock
private AccountsRepository accountsRepository;

@Mock
private NotificationService notificationService;

@InjectMocks
private AccountsService accountsService;

@BeforeEach
void setup() {
		MockitoAnnotations.openMocks(this);
	}

@Test
void contextLoads() {
}

@Test
void transferMoney() {
Account accountFrom = new Account("123", BigDecimal.valueOf(1000));
Account accountTo = new Account("456", BigDecimal.valueOf(500));

when(accountsRepository.getAccount("123")).thenReturn(accountFrom);
when(accountsRepository.getAccount("456")).thenReturn(accountTo);

accountsService.transfer("123", "456", BigDecimal.valueOf(200));

verify(notificationService).notifyAboutTransfer(eq(accountFrom), contains("Transferred 200"));
verify(notificationService).notifyAboutTransfer(eq(accountTo), contains("Received 200"));
verify(accountsRepository).updateAccount(accountFrom);
verify(accountsRepository).updateAccount(accountTo);

assert accountFrom.getBalance().equals(BigDecimal.valueOf(800)) : "Account 123 should have 800 after transfer";
assert accountTo.getBalance().equals(BigDecimal.valueOf(700)) : "Account 456 should have 700 after transfer";
}

@Test
void transferMoneyShouldFailForNegativeAmount() {
Account accountFrom = new Account("123", BigDecimal.valueOf(1000));
Account accountTo = new Account("456", BigDecimal.valueOf(500));

when(accountsRepository.getAccount("123")).thenReturn(accountFrom);
when(accountsRepository.getAccount("456")).thenReturn(accountTo);
try {
accountsService.transfer("123", "456", BigDecimal.valueOf(-200));
assert false : "Transfer of negative amount should throw an exception";
} catch (IllegalArgumentException e) {
assert e.getMessage().equals("Transfer amount must be positive") : "Exception message should indicate positive amount";
}
}

@Test
void transferMoneyShouldFailForInsufficientBalance() {
Account accountFrom = new Account("123", BigDecimal.valueOf(100));
Account accountTo = new Account("456", BigDecimal.valueOf(500));
when(accountsRepository.getAccount("123")).thenReturn(accountFrom);
when(accountsRepository.getAccount("456")).thenReturn(accountTo);

try {
accountsService.transfer("123", "456", BigDecimal.valueOf(200));
assert false : "Transfer with insufficient balance should throw an exception";
} catch (InsufficientBalanceException e) {
assert e.getMessage().equals("Insufficient balance") : "Exception message should indicate insufficient balance";
}
}
}
