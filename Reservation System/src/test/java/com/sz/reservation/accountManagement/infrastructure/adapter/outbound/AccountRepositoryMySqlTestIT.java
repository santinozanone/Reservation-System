package com.sz.reservation.accountManagement.infrastructure.adapter.outbound;

import com.github.f4b6a3.uuid.UuidCreator;
import com.sz.reservation.globalConfiguration.RootConfig;
import com.sz.reservation.accountManagement.application.dto.AccountCreationData;
import com.sz.reservation.accountManagement.domain.exception.EmailAlreadyRegisteredException;
import com.sz.reservation.accountManagement.domain.exception.UsernameAlreadyRegisteredException;
import com.sz.reservation.accountManagement.domain.model.Account;
import com.sz.reservation.accountManagement.domain.model.AccountVerificationToken;
import com.sz.reservation.accountManagement.domain.model.PhoneNumber;
import com.sz.reservation.accountManagement.domain.model.ProfilePicture;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.accountManagement.domain.service.HashingService;
import com.sz.reservation.accountManagement.infrastructure.service.BCryptPasswordHashingService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@DisplayName("Testing Account Repository MYSQL db ")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RootConfig.class)

@WebAppConfiguration
@ActiveProfiles(value = {"test","default"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountRepositoryMySqlTestIT {
    private String email;
    private String userId;

    private String username;
    private String name;
    private String surname;
    private String userVerificationToken;
    private LocalDate expirationDate;

    private String phoneNumberId;
    private PhoneNumber phoneNumber;

    private LocalDate birthDate;
    private String nationality;

    private HashingService hashingService;
    private String password;
    @Autowired
    private AccountRepository accountRepositoryMySql;

    @BeforeEach
    public void initializeVariables(){
        email = "inventedEmail@miau.com";
        userId = UuidCreator.getTimeOrderedEpoch().toString();
        username = "wolfofwallstreet";
        name = "jordan";
        surname = "belfort";
        userVerificationToken = UuidCreator.getTimeOrderedEpoch().toString();
        expirationDate = LocalDate.now().plusDays(7);

        phoneNumberId =  UuidCreator.getTimeOrderedEpoch().toString();
        phoneNumber = new PhoneNumber(phoneNumberId,"54","1111448899");
        birthDate = LocalDate.now().minusDays(10);
        nationality = "Argentina";
        hashingService = new BCryptPasswordHashingService();
        password =hashingService.hash("ultrasafepassword");
    }


    @Test
    @Transactional
    public void Should_createAccountCorrectly_When_ValidData(){
        //arrange
        AccountCreationData accountCreationData = new AccountCreationData(
                userId,
                username,
                name,
                surname,
                email,
                phoneNumber,
                birthDate,
                nationality,
                password,
                new ProfilePicture("C:\\Users\\losmelli\\Pictures\\pfp_2025-02-03T18-22-31-bb7c3d7b-656d-409a-ada7-f204b8933074.jpg"),
                new AccountVerificationToken(userId,userVerificationToken,expirationDate));

        //act
        accountRepositoryMySql.registerNotEnabledNotVerifiedUser(accountCreationData);

        //assert
        Optional<Account> account = accountRepositoryMySql.findAccountByEmail(email);
        Assertions.assertTrue(account.isPresent());
    }

    @Test
    @Transactional
    public void Should_ThrowEmailAlreadyRegisteredException_When_EmailAlreadyRegistered(){
        //arrange
        AccountCreationData accountCreationData = new AccountCreationData(
                userId,
                username,
                name,
                surname,
                email,
                phoneNumber,
                birthDate,
                nationality,
                password,
                new ProfilePicture("C:\\Users\\losmelli\\Pictures\\pfp_2025-02-03T18-22-31-bb7c3d7b-656d-409a-ada7-f204b8933074.jpg"),
                new AccountVerificationToken(userId,userVerificationToken,expirationDate));

        // generate new uuids
        userId = UuidCreator.getTimeOrderedEpoch().toString();
        username = "wolf"; // different username than first account
        phoneNumberId =  UuidCreator.getTimeOrderedEpoch().toString();
        phoneNumber = new PhoneNumber(phoneNumberId,"+54","1111448898");

        AccountCreationData dataWithDifferentUsername = new AccountCreationData(
                userId,
                username,
                name,
                surname,
                email,
                phoneNumber,
                birthDate,
                nationality,
                password,
                new ProfilePicture("C:\\Users\\losmelli\\Pictures\\pfp_2025-02-03T18-22-31-bb7c3d7b-656d-409a-ada7-f204b8933074.jpg"),
                new AccountVerificationToken(userId,userVerificationToken,expirationDate));

        //act
        accountRepositoryMySql.registerNotEnabledNotVerifiedUser(accountCreationData);

        //assert
        Optional<Account> account = accountRepositoryMySql.findAccountByEmail(email);
        Assertions.assertTrue(account.isPresent()); // assert first registration is done correctly

        Assertions.assertThrows(EmailAlreadyRegisteredException.class,()->{ // assert second user fails
            accountRepositoryMySql.registerNotEnabledNotVerifiedUser(dataWithDifferentUsername);
        });
    }

    @Test
    @Transactional
    public void Should_ThrowUsernameAlreadyRegisteredException_When_UsernameAlreadyRegistered(){
        //arrange
        AccountCreationData accountCreationData = new AccountCreationData(
                userId,
                username,
                name,
                surname,
                email,
                phoneNumber,
                birthDate,
                nationality,
                password,
                new ProfilePicture("C:\\Users\\losmelli\\Pictures\\pfp_2025-02-03T18-22-31-bb7c3d7b-656d-409a-ada7-f204b8933074.jpg"),
                new AccountVerificationToken(userId,userVerificationToken,expirationDate));

        // generate new uuids
        userId = UuidCreator.getTimeOrderedEpoch().toString();
        phoneNumberId =  UuidCreator.getTimeOrderedEpoch().toString();
        phoneNumber = new PhoneNumber(phoneNumberId,"+54","1111448898");
        String secondEmail = "inventedEmail2@miau.com";
        AccountCreationData dataWithDifferentEmail = new AccountCreationData(
                userId,
                username,
                name,
                surname,
                secondEmail,
                phoneNumber,
                LocalDate.now(),
                nationality,
                password,
                new ProfilePicture("C:\\Users\\losmelli\\Pictures\\pfp_2025-02-03T18-22-31-bb7c3d7b-656d-409a-ada7-f204b8933074.jpg"),
                new AccountVerificationToken(userId,userVerificationToken,expirationDate));

        //act
        accountRepositoryMySql.registerNotEnabledNotVerifiedUser(accountCreationData);

        //assert
        Optional<Account> account = accountRepositoryMySql.findAccountByEmail(email);
        Assertions.assertTrue(account.isPresent()); // assert first registration is done correctly


        Assertions.assertThrows(UsernameAlreadyRegisteredException.class,()->{ // assert second user fails
            accountRepositoryMySql.registerNotEnabledNotVerifiedUser(dataWithDifferentEmail);
        });
    }

    @Test
    @Transactional
    public void Should_ReturnEmptyOptional_When_AccountNotExistsWithEmail(){
        //arrange
        String email = "notExistentEmail@gmail.com";
        //assert
        Optional<Account> account = accountRepositoryMySql.findAccountByEmail(email);
        Assertions.assertFalse(account.isPresent());
    }


    @Test
    public void Should_ThrowIllegalArgumentException_When_FindByEmailIsNull(){
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            accountRepositoryMySql.findAccountByEmail(null);
        });
    }

    @Test
    public void Should_ThrowIllegalArgumentException_When_CreationDataIsNull(){
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            accountRepositoryMySql.registerNotEnabledNotVerifiedUser(null);
        });
    }


}