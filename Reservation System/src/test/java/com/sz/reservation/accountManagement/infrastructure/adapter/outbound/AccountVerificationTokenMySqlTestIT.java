package com.sz.reservation.accountManagement.infrastructure.adapter.outbound;

import com.github.f4b6a3.uuid.UuidCreator;
import com.sz.reservation.accountManagement.application.dto.AccountCreationData;
import com.sz.reservation.accountManagement.domain.model.Account;
import com.sz.reservation.accountManagement.domain.model.AccountVerificationToken;
import com.sz.reservation.accountManagement.domain.model.PhoneNumber;
import com.sz.reservation.accountManagement.domain.model.ProfilePicture;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountVerificationTokenRepository;
import com.sz.reservation.accountManagement.domain.service.HashingService;
import com.sz.reservation.accountManagement.infrastructure.service.BCryptPasswordHashingService;
import com.sz.reservation.configuration.RootConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("integration testing account verification token mysql db")
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = RootConfig.class)

@ActiveProfiles({"test","default"})
class AccountVerificationTokenMySqlTestIT {

    @Autowired
    private AccountVerificationTokenRepository verificationTokenRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @Transactional
    public void Should_ReturnToken_When_TokenExistsInDb(){
        //arrange
        String verificationToken = UuidCreator.getTimeOrderedEpoch().toString();
        String userId = UuidCreator.getTimeOrderedEpoch().toString();
        LocalDate expirationDate = LocalDate.now().plusDays(7);
        AccountVerificationToken accountVerificationToken = new AccountVerificationToken(userId,verificationToken,expirationDate);

        //act
        insertUser(userId,verificationToken);
        verificationTokenRepository.save(accountVerificationToken);

        //assert
        Optional<AccountVerificationToken> verificationTokenFromDb = verificationTokenRepository.findByToken(accountVerificationToken.getToken());
        assertTrue(verificationTokenFromDb.isPresent());
        assertEquals(verificationToken, verificationTokenFromDb.get().getToken());
        assertEquals(userId,verificationTokenFromDb.get().getUserId());
        assertEquals(expirationDate,verificationTokenFromDb.get().getExpirationDate());
    }


    @Test
    public void Should_ReturnEmptyOption_When_TokenDoesNotExistsInDb(){
        //arrange
        String tokenNotInDb =  "01854f09-742d-7d86-a3da-b0127c8facc4";

        //assert
        Optional<AccountVerificationToken> verificationTokenFromDb = verificationTokenRepository.findByToken(tokenNotInDb);
        assertTrue(verificationTokenFromDb.isEmpty());
    }

    @Test
    public void Should_ThrowIllegalArgumentException_When_SavingAccountVerificationTokenIsNull(){
        //act and assert
        assertThrows(IllegalArgumentException.class, () -> {
            verificationTokenRepository.save(null);
        });
    }


    @Test
    public void Should_ThrowIllegalArgumentException_When_FindingNullToken(){
        //act and assert
        assertThrows(IllegalArgumentException.class, () -> {
            verificationTokenRepository.findByToken(null);
        });
    }

    @Test
    public void Should_ThrowIllegalArgumentException_When_FindingEmptyToken(){
        //act and assert
        assertThrows(IllegalArgumentException.class, () -> {
            verificationTokenRepository.findByToken(null);
        });
    }

    private void insertUser(String userId,String userVerificationToken){
        //arrange
        String email = "inventedEmail@miau.com";
        LocalDate expirationDate = LocalDate.now().plusDays(7);

        String phoneNumberId =  UuidCreator.getTimeOrderedEpoch().toString();
        PhoneNumber phoneNumber = new PhoneNumber(phoneNumberId,"+54","1111448899");

        HashingService hashingService = new BCryptPasswordHashingService();
        String password =hashingService.hash("ultrasafepassword");

        AccountVerificationToken verificationToken = new AccountVerificationToken(userId,userVerificationToken,expirationDate);

        AccountCreationData accountCreationData = new AccountCreationData(
                userId,
                "wolfofwallstreet",
                "jordan",
                "belfort",
                email,
                phoneNumber,
                LocalDate.now(),
                "Argentina",
                password,
                new ProfilePicture("C:\\Users\\losmelli\\Pictures\\pfp_2025-02-03T18-22-31-bb7c3d7b-656d-409a-ada7-f204b8933074.jpg"),
                verificationToken);

        //act, (insert user)
        accountRepository.registerNotEnabledNotVerifiedUser(accountCreationData);
        //assert
        Optional<Account> account = accountRepository.findAccountByEmail(accountCreationData.getEmail());
        Assertions.assertTrue(account.isPresent());
    }
}