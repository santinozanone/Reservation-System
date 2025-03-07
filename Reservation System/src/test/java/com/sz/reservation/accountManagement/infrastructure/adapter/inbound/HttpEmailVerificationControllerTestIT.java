package com.sz.reservation.accountManagement.infrastructure.adapter.inbound;

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
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RootConfig.class)
@WebAppConfiguration
@ActiveProfiles(value = {"test","default"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Integration testing HttpEmailVerificationControllerTest")
public class HttpEmailVerificationControllerTestIT {

    @Autowired
    private AccountVerificationTokenRepository verificationTokenRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private WebApplicationContext context;
    private WebTestClient client;

    @BeforeAll
    private void instantiatingValidator(){
        client = MockMvcWebTestClient.bindToApplicationContext(context).build();
    }


    @Test
    @Transactional
    public void Should_enabledAndVerifyAccountCorrectly_When_ValidToken(){
        //arrange
        String email = "inventedEmail@miau.com";
        String verificationToken = UuidCreator.getTimeOrderedEpoch().toString();
        String userId = UuidCreator.getTimeOrderedEpoch().toString();
        LocalDate expirationDate = LocalDate.now().plusDays(7);

        AccountVerificationToken accountVerificationToken = new AccountVerificationToken(userId,verificationToken,expirationDate);

        //act and assert
        insertUser(userId,email,verificationToken);
        verificationTokenRepository.save(accountVerificationToken);
        Assertions.assertTrue(verificationTokenRepository.findByToken(accountVerificationToken.getToken()).isPresent());

        client.post().uri(uriBuilder -> uriBuilder
                .path("/api/v1/account/verify").queryParam("token",accountVerificationToken.getToken()).build()).exchange().expectStatus().isOk();


        //assert account is enabled
        Optional<Account> OptionalAccount = accountRepository.findAccountByEmail(email);
        Assertions.assertTrue(OptionalAccount.isPresent()); // account must exists
        Assertions.assertTrue(OptionalAccount.get().isEnabled()); // must be enabled
        Assertions.assertTrue(OptionalAccount.get().isVerified()); // must be verified
    }


    @Test
    @Transactional
    public void Should_ReturnBadRequest_When_TokenIsNotInDB(){
        //arrange
        String email = "inventedEmail@miau.com";
        String userId = UuidCreator.getTimeOrderedEpoch().toString();
        String tokenNotInDb =  "01854f09-742d-7d86-a3da-b0127c8facc4";
        String tokenToInsert = "01954f09-742d-7d86-a3da-b0127c8facc4"; // 36 characters token
        LocalDate expirationDate = LocalDate.now().plusDays(7);
        AccountVerificationToken verificationToken = new AccountVerificationToken(userId,tokenToInsert,expirationDate);

        //act and assert
        insertUser(userId,email,tokenToInsert);
        verificationTokenRepository.save(verificationToken);
        Assertions.assertTrue(verificationTokenRepository.findByToken(verificationToken.getToken()).isPresent());

        client.post().uri(uriBuilder -> uriBuilder // sending request with token that doesnt exists
                .path("/api/v1/account/verify").queryParam("token",tokenNotInDb).build()).exchange().expectStatus().isBadRequest();


        //assert account is not enabled and not verified
        Optional<Account> OptionalAccount = accountRepository.findAccountByEmail(email);
        Assertions.assertTrue(OptionalAccount.isPresent()); // account must exists
        Assertions.assertFalse(OptionalAccount.get().isEnabled()); // must not be enabled
        Assertions.assertFalse(OptionalAccount.get().isVerified()); // must not be verified
    }


    @Test
    @Transactional
    public void Should_ReturnBadRequest_When_TokenProvidedIsNotValidFormat() {
        String invalidToken = "01954f09-742d-7d86-a3dab-0127c8facc4"; // invalidFormat
        //assert
        client.post().uri(uriBuilder -> uriBuilder // sending request with invalid format
                .path("/api/v1/account/verify").queryParam("token", invalidToken).build()).exchange().expectStatus().isBadRequest();
    }

    @Test
    @Transactional
    public void Should_ReturnBadRequest_When_TokenIsExpired() {
        //arrange
        String email = "inventedEmail@miau.com";
        String userId = UuidCreator.getTimeOrderedEpoch().toString();
        String expiredToken = "01954f09-742d-7d86-a3da-b0127c8facc4"; // 36 characters token
        LocalDate expiredDate = LocalDate.now().minusYears(1);
        AccountVerificationToken expiredVerificationToken = new AccountVerificationToken(userId,expiredToken,expiredDate);


        //act
        insertUser(userId,email,expiredToken); // save user
        verificationTokenRepository.save(expiredVerificationToken); // insert expired token in db

        //assert
        Assertions.assertTrue(verificationTokenRepository.findByToken(expiredToken).isPresent()); // assert token exists
        client.post().uri(uriBuilder -> uriBuilder // sending request with expired token
                .path("/api/v1/account/verify").queryParam("token", expiredToken).build()).exchange().expectStatus().isBadRequest();

        Optional<Account> OptionalAccount = accountRepository.findAccountByEmail(email);
        Assertions.assertTrue(OptionalAccount.isPresent()); // account must exists
        Assertions.assertFalse(OptionalAccount.get().isEnabled()); // must not be enabled
        Assertions.assertFalse(OptionalAccount.get().isVerified()); // must not be verified
    }

    @Test
    @Transactional
    public void Should_ReturnBadRequest_When_AlreadyVerifiedUser() {
        //arrange
        String email = "inventedEmail@miau.com";
        String userId = UuidCreator.getTimeOrderedEpoch().toString();
        String token = "01954f09-742d-7d86-a3da-b0127c8facc4"; // 36 characters token
        LocalDate date = LocalDate.now().plusDays(7);
        AccountVerificationToken verificationToken = new AccountVerificationToken(userId,token,date);


        //act and assert
        insertUser(userId,email,token); // save user
        verificationTokenRepository.save(verificationToken); // insert token in db
        Assertions.assertTrue(verificationTokenRepository.findByToken(token).isPresent()); // assert token exists

        client.post().uri(uriBuilder -> uriBuilder // verify token
                .path("/api/v1/account/verify").queryParam("token", token).build()).exchange().expectStatus().isOk();

        client.post().uri(uriBuilder -> uriBuilder // verify token again
                .path("/api/v1/account/verify").queryParam("token", token).build()).exchange().expectStatus().isBadRequest();


        Optional<Account> OptionalAccount = accountRepository.findAccountByEmail(email);
        Assertions.assertTrue(OptionalAccount.isPresent()); // account must exists
        Assertions.assertTrue(OptionalAccount.get().isEnabled()); // must be enabled
        Assertions.assertTrue(OptionalAccount.get().isVerified()); // must be verified
    }



    private void insertUser(String userId,String email,String userVerificationToken){
        //arrange
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
