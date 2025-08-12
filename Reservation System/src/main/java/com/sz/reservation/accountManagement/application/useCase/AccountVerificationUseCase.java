package com.sz.reservation.accountManagement.application.useCase;

import com.github.f4b6a3.uuid.UuidCreator;
import com.sz.reservation.accountManagement.domain.event.AccountVerifiedEvent;
import com.sz.reservation.accountManagement.domain.exception.AccountAlreadyVerifiedException;
import com.sz.reservation.accountManagement.domain.exception.InvalidTokenException;
import com.sz.reservation.accountManagement.domain.model.Account;
import com.sz.reservation.accountManagement.domain.model.AccountVerificationToken;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountVerificationTokenRepository;
import com.sz.reservation.accountManagement.domain.port.outbound.VerificationTokenEmailSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDate;
import java.util.Optional;

public class AccountVerificationUseCase {
    private Logger logger = LogManager.getLogger(AccountVerificationUseCase.class);
    private AccountRepository accountRepository;
    private AccountVerificationTokenRepository tokenRepository;
    private VerificationTokenEmailSender verificationTokenEmailSender;
    private ApplicationEventPublisher eventPublisher;

    public AccountVerificationUseCase(AccountRepository accountRepository,AccountVerificationTokenRepository tokenRepository,
                                      VerificationTokenEmailSender verificationTokenEmailSender,
                                      ApplicationEventPublisher eventPublisher) {
        this.accountRepository = accountRepository;
        this.tokenRepository = tokenRepository;
        this.verificationTokenEmailSender = verificationTokenEmailSender;
        this.eventPublisher = eventPublisher;
    }

    @Transactional("account.transactionManager")
    public void verifyAccount(String token) {
        logger.info("attempting to verify token :{}",token);

        //validate token
        AccountVerificationToken verificationToken = validateToken(token);
        // if token exists and is valid, retrieve account
        Optional<Account> optionalAccount = accountRepository.findAccountByUserId(verificationToken.getUserId());
        if (optionalAccount.isEmpty()){
            logger.error("UserId:{} , obtained from verification token:{}, IS INEXISTENT ", verificationToken.getUserId(),verificationToken.getToken());
            throw new RuntimeException("UserId " +verificationToken.getUserId() + " obtained from verification token does not exists") ; // maybe should be custom exception
        }
        Account account = optionalAccount.get();
        if (account.isVerified() ) throw new AccountAlreadyVerifiedException(account.getId());

        account.setAccountVerified(); // set account verified to true
        account.enableAccount(); // set account enabled to true
        accountRepository.updateAccount(account);


        logger.info("account with id:{}  , verified and enabled correctly", account.getId());

        logger.debug("TRANS: {}", TransactionSynchronizationManager.isActualTransactionActive());


        logger.info("TRANS: {}", TransactionSynchronizationManager.isActualTransactionActive());

        logger.info("Account verified event published for id:{}", account.getId());
        eventPublisher.publishEvent(new AccountVerifiedEvent(account.getId(),account.getUniqueUsername(),
                account.getName(),account.getSurname(),account.getUniqueEmail(),account.isEnabled()));

    }

    @Transactional("account.transactionManager")
    public void resendVerificationTokenEmail(String token){
        logger.info("attempting to resend verification token, old value:{}",token);
        Optional<AccountVerificationToken> optionalVerificationToken = tokenRepository.findByToken(token);
        if (optionalVerificationToken.isEmpty()) {
            logger.debug("token does not exists in db:{}",token);
            throw new InvalidTokenException(token);
        }
        AccountVerificationToken verificationToken = optionalVerificationToken.get();
        if (!verificationToken.isExpired()) throw new InvalidTokenException("token is still valid",token); // if token is not expired cannot resend new one

        Optional<Account> optionalAccount = accountRepository.findAccountByUserId(verificationToken.getUserId());
        if (optionalAccount.isEmpty()){
            logger.error("UserId:{} , obtained from verification token:{}, IS INEXISTENT ", verificationToken.getUserId(),verificationToken.getToken());
            throw new RuntimeException("UserId " +verificationToken.getUserId() + " obtained from verification token does not exists") ; // maybe should be custom exception
        }
        Account account = optionalAccount.get();

        //create new token
        LocalDate expirationDate = LocalDate.now().plusDays(7);
        String accountVerificationToken = UuidCreator.getTimeOrderedEpoch().toString();
        AccountVerificationToken newVerificationToken = new AccountVerificationToken(verificationToken.getUserId(),accountVerificationToken ,expirationDate);

        //update token in db
        tokenRepository.update(token,newVerificationToken);

        //resend email
        verificationTokenEmailSender.sendEmailTo(account.getUniqueEmail(),account.getUniqueUsername(),newVerificationToken.getToken());
        logger.info("token updated correctly, email sent correctly to: {}",account.getUniqueEmail());
    }


    //returns an accountVerificationToken if token is valid
    private AccountVerificationToken validateToken(String token){
        Optional<AccountVerificationToken> optionalVerificationToken = tokenRepository.findByToken(token);
        if (optionalVerificationToken.isEmpty()) {
            logger.debug("token does not exists in db:{}",token);
            throw new InvalidTokenException(token);
        }
        AccountVerificationToken verificationToken = optionalVerificationToken.get();
        if (verificationToken.isExpired()){
            logger.debug("the token:{}, is Expired",token);
            throw new InvalidTokenException(token);
        }
        return verificationToken;
    }

}
