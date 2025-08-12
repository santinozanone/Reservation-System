package com.sz.reservation.listingManagement.application.useCase.eventListener;

import com.sz.reservation.accountManagement.domain.event.AccountDeletedEvent;
import com.sz.reservation.accountManagement.domain.event.AccountUpdatedEvent;
import com.sz.reservation.listingManagement.domain.Account;
import com.sz.reservation.listingManagement.domain.port.outbound.AccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

//@Service
public class AccountDeletedEventListener {
    private Logger logger = LogManager.getLogger(AccountVerifiedEventListener.class);
    private AccountRepository accountRepository;

    @Autowired
    public AccountDeletedEventListener(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Async
    @Transactional(transactionManager = "listing.transactionManager", propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT   )
    public void handleAccountDeletedEventListener(AccountDeletedEvent accountDeletedEvent){
        logger.info("Received AccountDeletedEvent with accountId :{}",accountDeletedEvent.getAccountId());
        //fetch account
        Account fetchedAccount = accountRepository.findById(accountDeletedEvent.getAccountId()).orElseThrow(
                () -> new EmptyResultDataAccessException("Account Not Found",1));

        fetchedAccount.disableAccount();

        try {
            accountRepository.update(fetchedAccount);
        }catch (DataAccessException e){
            logger.error("FAILED to consume AccountDeletedEvent for account ID: {} ",accountDeletedEvent.getAccountId());
            throw e;
        }
        logger.info("AccountDeletedEvent consumed CORRECTLY");
    }

}
