package com.sz.reservation.accountManagement.infrastructure.adapter.outbound;

import com.github.f4b6a3.uuid.UuidCreator;
import com.sz.reservation.accountManagement.configuration.AccountConfig;
import com.sz.reservation.accountManagement.domain.exception.EmailAlreadyRegisteredException;
import com.sz.reservation.accountManagement.domain.exception.UsernameAlreadyRegisteredException;
import com.sz.reservation.accountManagement.domain.model.Account;
import com.sz.reservation.accountManagement.domain.model.PhoneNumber;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.accountManagement.domain.service.HashingService;
import com.sz.reservation.accountManagement.infrastructure.service.BCryptPasswordHashingService;
import com.sz.reservation.globalConfiguration.RootConfig;
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
@ContextConfiguration(classes = {RootConfig.class, AccountConfig.class})

@WebAppConfiguration
@ActiveProfiles(value = {"test","default"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class AccountRepositoryMySqlTestIT {
    private String email;
    private String userId;

    private String username;
    private String name;
    private String surname;

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
        Account account = createNotVerifiedAccount();

        //act
        accountRepositoryMySql.createAccount(account);

        //assert
        Optional<Account> optionalAccount = accountRepositoryMySql.findAccountByEmail(email);
        Assertions.assertTrue(optionalAccount.isPresent());
    }

    @Test
    @Transactional
    public void Should_ThrowEmailAlreadyRegisteredException_When_EmailAlreadyRegistered(){
        //arrange
        Account account = createNotVerifiedAccount();

        // generate new uuids
        userId = UuidCreator.getTimeOrderedEpoch().toString();
        username = "wolfs"; // different username than first account
        phoneNumberId =  UuidCreator.getTimeOrderedEpoch().toString();
        phoneNumber = new PhoneNumber(phoneNumberId,"+54","1111448898");

        Account accountWithDifferentUsername = createNotVerifiedAccount();

        //act
        accountRepositoryMySql.createAccount(account);

        //assert
        Optional<Account> OptionalAccount = accountRepositoryMySql.findAccountByEmail(email);
        Assertions.assertTrue(OptionalAccount.isPresent()); // assert first registration is done correctly

        Assertions.assertThrows(EmailAlreadyRegisteredException.class,()->{ // assert second user fails
            accountRepositoryMySql.createAccount(accountWithDifferentUsername);
        });
    }

    @Test
    @Transactional
    public void Should_ThrowUsernameAlreadyRegisteredException_When_UsernameAlreadyRegistered(){
        //arrange
        Account account = createNotVerifiedAccount();
        String unchangedEmail = email;
        // generate new uuids
        userId = UuidCreator.getTimeOrderedEpoch().toString();
        phoneNumberId =  UuidCreator.getTimeOrderedEpoch().toString();
        phoneNumber = new PhoneNumber(phoneNumberId,"+54","1111448898");
        email = "inventedEmail2@miau.com";

        Account accountWithDifferentEmail = createNotVerifiedAccount();

        //act
        accountRepositoryMySql.createAccount(account);

        //assert
        Optional<Account> optionalAccount = accountRepositoryMySql.findAccountByEmail(unchangedEmail);
        Assertions.assertTrue(optionalAccount.isPresent()); // assert first registration is done correctly

        Assertions.assertThrows(UsernameAlreadyRegisteredException.class,()->{ // assert second user fails
            accountRepositoryMySql.createAccount(accountWithDifferentEmail);
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
            accountRepositoryMySql.createAccount(null);
        });
    }


    private Account createNotVerifiedAccount(){
        return new Account(
                userId,
                username,
                name,
                surname,
                email,
                phoneNumber,
                birthDate,
                password,
                false,
                false
        );
    }

}