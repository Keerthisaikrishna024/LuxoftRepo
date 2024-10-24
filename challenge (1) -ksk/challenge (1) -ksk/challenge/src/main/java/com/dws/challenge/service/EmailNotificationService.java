package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailNotificationService implements NotificationService {

  @Override
  public void notifyAboutTransfer(Account account, String transferDescription) {
    log.info("Sending notification to owner of {}: {}", account.getAccountId(), transferDescription);
  }
}
