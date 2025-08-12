package com.sz.reservation.listingManagement.application.useCase.eventListener;

import com.sz.reservation.accountManagement.domain.event.AccountVerifiedEvent;
import com.sz.reservation.listingManagement.domain.Account;
import com.sz.reservation.listingManagement.domain.port.outbound.AccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;


//@Component
public class AccountVerifiedEventListener {
    private Logger logger = LogManager.getLogger(AccountVerifiedEventListener.class);
    private AccountRepository accountRepository;

    @Autowired
    public AccountVerifiedEventListener(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Async
    @Transactional(transactionManager = "listing.transactionManager", propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT   )
    public void handleAccountVerifiedEvent(AccountVerifiedEvent accountVerifiedEvent){
        logger.info("Received AccountVerifiedEvent with accountId :{}",accountVerifiedEvent.getAccountId());
        // create account in the repository
        Account account = new Account(accountVerifiedEvent.getAccountId(),accountVerifiedEvent.getUsername(),
                accountVerifiedEvent.getName(),accountVerifiedEvent.getSurname(),
                accountVerifiedEvent.getEmail(),accountVerifiedEvent.isEnabled());
        try {
            accountRepository.save(account);
        }catch (DataAccessException e){
            logger.error("FAILED to consume AccountVerifiedEvent for account ID: {} ",accountVerifiedEvent.getAccountId());
            throw e;
        }
        logger.info("AccountVerifiedEvent consumed CORRECTLY");
    }
}
