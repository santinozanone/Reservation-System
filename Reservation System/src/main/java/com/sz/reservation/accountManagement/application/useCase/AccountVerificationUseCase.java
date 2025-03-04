package com.sz.reservation.accountManagement.application.useCase;

import com.sz.reservation.accountManagement.domain.exception.AccountAlreadyVerifiedException;
import com.sz.reservation.accountManagement.domain.exception.InvalidTokenException;
import com.sz.reservation.accountManagement.domain.model.Account;
import com.sz.reservation.accountManagement.domain.model.AccountVerificationToken;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountVerificationTokenRepository;
import com.sz.reservation.accountManagement.domain.port.outbound.VerificationTokenEmailSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class AccountVerificationUseCase {
    private Logger logger = LogManager.getLogger(AccountVerificationUseCase.class);
    private AccountRepository accountRepository;
    private AccountVerificationTokenRepository tokenRepository;

    public AccountVerificationUseCase(AccountRepository accountRepository,AccountVerificationTokenRepository tokenRepository) {
        this.accountRepository = accountRepository;
        this.tokenRepository = tokenRepository;
    }

    public void verifyAccount(String token){
        logger.info("attempting to verify token :{}",token);
        //validate token
        AccountVerificationToken verificationToken = validateToken(token);
        // if token exists and is valid, retrieve account
        Optional<Account> optionalAccount = accountRepository.findAccountByUserId(verificationToken.getUserId());
        if (optionalAccount.isEmpty()){
            logger.error("UserId:{} , obtained from verification token, IS INEXISTENT ", verificationToken.getUserId());
            throw new RuntimeException("UserId " +verificationToken.getUserId() + " obtained from verification token does not exists") ; // maybe should be custom exception
        }
        Account account = optionalAccount.get();
        if (account.isVerified() ) throw new AccountAlreadyVerifiedException(account.getId());
        account.setAccountVerified(); // set account verified to true
        account.enableAccount(); // set account enabled to true
        accountRepository.updateAccount(account);
        logger.info("account with id:{}  , verified and enabled correctly", account.getId());

    }
    private AccountVerificationToken validateToken(String token){
        Optional<AccountVerificationToken> optionalVerificationToken = tokenRepository.findByToken(token);
        if (optionalVerificationToken.isEmpty()) {
            logger.debug("token does not exists in db:{}",token);
            throw new InvalidTokenException(token);
        }
        AccountVerificationToken verificationToken = optionalVerificationToken.get();
        if (!verificationToken.isValid()){
            logger.debug("invalid token:{}",token);
            throw new InvalidTokenException(token);
        }
        return verificationToken;
    }

}
