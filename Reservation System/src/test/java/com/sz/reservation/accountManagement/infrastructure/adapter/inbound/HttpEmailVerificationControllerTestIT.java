package com.sz.reservation.accountManagement.infrastructure.adapter.inbound;

import com.github.f4b6a3.uuid.UuidCreator;
import com.sz.reservation.accountManagement.application.dto.AccountCreationData;
import com.sz.reservation.accountManagement.application.useCase.AccountVerificationUseCase;
import com.sz.reservation.accountManagement.domain.model.Account;
import com.sz.reservation.accountManagement.domain.model.AccountVerificationToken;
import com.sz.reservation.accountManagement.domain.model.PhoneNumber;
import com.sz.reservation.accountManagement.domain.model.ProfilePicture;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountVerificationTokenRepository;
import com.sz.reservation.accountManagement.domain.service.HashingService;
import com.sz.reservation.accountManagement.infrastructure.service.BCryptPasswordHashingService;
import com.sz.reservation.configuration.RootConfig;
import com.sz.reservation.configuration.ServletConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
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
@ContextHierarchy({
        @ContextConfiguration(classes = RootConfig.class),
        @ContextConfiguration(classes = ServletConfig.class)
})
@WebAppConfiguration
@ActiveProfiles(value = {"test","default"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Integration testing HttpEmailVerificationControllerTest")
public class HttpEmailVerificationControllerTestIT {


    @Autowired
    private  HttpEmailVerificationController controller;

    private Logger logger = LogManager.getLogger(HttpEmailVerificationControllerTestIT.class);

    @Autowired
    private AccountVerificationTokenRepository verificationTokenRepository;

    @Autowired
    private AccountVerificationUseCase useCase;
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
        AccountCreationData accountCreationData = accountCreationData();
        //act, (insert user)
        accountRepository.registerNotEnabledNotVerifiedUser(accountCreationData);
        //assert
        Optional<Account> account = accountRepository.findAccountByEmail(accountCreationData.getEmail());
        Assertions.assertTrue(account.isPresent());


        AccountVerificationToken verificationToken = accountCreationData.getVerificationToken();
        //act
        verificationTokenRepository.save(verificationToken);
        //assert
        Assertions.assertTrue(verificationTokenRepository.findByToken(verificationToken.getToken()).isPresent());


        //act and assert
        client.post().uri(uriBuilder -> uriBuilder
                .path("/api/v1/account/verify").queryParam("token",verificationToken.getToken()).build()).exchange().expectStatus().isOk();




        //assert account is enabled
        Optional<Account> OptionalAccount = accountRepository.findAccountByEmail(accountCreationData.getEmail());
        Assertions.assertTrue(OptionalAccount.isPresent());
        Assertions.assertTrue(OptionalAccount.get().isEnabled());
        Assertions.assertTrue(OptionalAccount.get().isVerified());
    }


    public AccountCreationData accountCreationData(){
        //arrange
        String email = "inventedEmail@miau.com";
        String userId = UuidCreator.getTimeOrderedEpoch().toString();
        String userVerificationToken = UuidCreator.getTimeOrderedEpoch().toString();
        LocalDate expirationDate = LocalDate.now().plusDays(7);

        String phoneNumberId =  UuidCreator.getTimeOrderedEpoch().toString();
        PhoneNumber phoneNumber = new PhoneNumber(phoneNumberId,"+54","1111448899");

        HashingService hashingService = new BCryptPasswordHashingService();
        String password =hashingService.hash("ultrasafepassword");

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
                new AccountVerificationToken(userId,userVerificationToken,expirationDate));

        return accountCreationData;
    }



}
