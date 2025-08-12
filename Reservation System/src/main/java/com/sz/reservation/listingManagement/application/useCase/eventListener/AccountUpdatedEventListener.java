package com.sz.reservation.listingManagement.application.useCase.eventListener;

import com.sz.reservation.accountManagement.domain.event.AccountUpdatedEvent;
import com.sz.reservation.listingManagement.domain.Account;
import com.sz.reservation.listingManagement.domain.port.outbound.AccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

//@Component
public class AccountUpdatedEventListener {
    private Logger logger = LogManager.getLogger(AccountVerifiedEventListener.class);
    private AccountRepository accountRepository;

    @Autowired
    public AccountUpdatedEventListener(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Async
    @Transactional(transactionManager = "listing.transactionManager", propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT   )
    public void handleAccountUpdatedEventListener(AccountUpdatedEvent accountUpdatedEvent){
        logger.info("Received AccountUpdatedEvent with accountId :{}",accountUpdatedEvent.getAccountId());
        //fetch account
        Account fetchedAccount = accountRepository.findById(accountUpdatedEvent.getAccountId()).orElseThrow(
                () -> new EmptyResultDataAccessException("Account Not Found",1));

        Account updatedAccount = new Account(fetchedAccount.getId(),accountUpdatedEvent.getUsername(),
                accountUpdatedEvent.getName(),accountUpdatedEvent.getSurname(),fetchedAccount.getEmail(),fetchedAccount.isEnabled());
        try {
            accountRepository.update(updatedAccount);
        }catch (DataAccessException e){
            logger.error("FAILED to consume AccountUpdatedEvent for account ID: {} ",accountUpdatedEvent.getAccountId());
            throw e;
        }
        logger.info("AccountUpdatedEvent consumed CORRECTLY");

    }

}
