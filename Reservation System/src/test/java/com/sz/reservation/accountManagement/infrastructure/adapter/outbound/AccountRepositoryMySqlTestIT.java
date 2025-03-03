package com.sz.reservation.accountManagement.infrastructure.adapter.outbound;

import com.github.f4b6a3.uuid.UuidCreator;
import com.sz.reservation.configuration.RootConfig;
import com.sz.reservation.configuration.ServletConfig;
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
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@DisplayName("Testing Account db ")
@ExtendWith(SpringExtension.class)
@ContextHierarchy({
        @ContextConfiguration(classes = RootConfig.class),
        @ContextConfiguration(classes = ServletConfig.class)
})
@WebAppConfiguration
@ActiveProfiles(value = {"test","default"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountRepositoryMySqlTestIT {

    @Autowired
    private AccountRepository accountRepositoryMySql ;



    @Test
    @Transactional
    public void Should_createAccountCorrectly_When_ValidData(){
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
        String email = "inventedEmail@miau.com";
        String userId = UuidCreator.getTimeOrderedEpoch().toString();
        String userVerificationToken = UuidCreator.getTimeOrderedEpoch().toString();
        LocalDate expirationDate = LocalDate.now().plusDays(7);

        String phoneNumberId =  UuidCreator.getTimeOrderedEpoch().toString();
        PhoneNumber phoneNumber = new PhoneNumber(phoneNumberId,"+54","1111448898");

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

        // generate new uuids
        userId = UuidCreator.getTimeOrderedEpoch().toString();
        phoneNumberId =  UuidCreator.getTimeOrderedEpoch().toString();
        phoneNumber = new PhoneNumber(phoneNumberId,"+54","1111448898");
        AccountCreationData dataWithDifferentUsername = new AccountCreationData(
                userId,
                "wolf",
                "jordan",
                "belfort",
                email,
                phoneNumber,
                LocalDate.now(),
                "Argentina",
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
        String email = "inventedEmail@miau.com";
        String userId = UuidCreator.getTimeOrderedEpoch().toString();
        String userVerificationToken = UuidCreator.getTimeOrderedEpoch().toString();
        LocalDate expirationDate = LocalDate.now().plusDays(7);

        String phoneNumberId =  UuidCreator.getTimeOrderedEpoch().toString();
        PhoneNumber phoneNumber = new PhoneNumber(phoneNumberId,"+54","1111448897");

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

        // generate new uuids
        userId = UuidCreator.getTimeOrderedEpoch().toString();
        phoneNumberId =  UuidCreator.getTimeOrderedEpoch().toString();
        phoneNumber = new PhoneNumber(phoneNumberId,"+54","1111448898");
        AccountCreationData dataWithDifferentEmail = new AccountCreationData(
                userId,
                "wolfofwallstreet",
                "jordan",
                "belfort",
                "inventedEmail2@miau.com",
                phoneNumber,
                LocalDate.now(),
                "Argentina",
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
    @Transactional
    public void Should_ReturnEmptyOptional_When_AccountNotExistsWithUsername(){
        //arrange
        String username = "tomCruise";
        //assert
        Optional<Account> account = accountRepositoryMySql.findAccountByUsername(username);
        Assertions.assertFalse(account.isPresent());
    }


    @Test
    public void Should_ThrowIllegalArgumentException_When_FindByUsernameIsNull(){
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            accountRepositoryMySql.findAccountByUsername(null);
        });
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