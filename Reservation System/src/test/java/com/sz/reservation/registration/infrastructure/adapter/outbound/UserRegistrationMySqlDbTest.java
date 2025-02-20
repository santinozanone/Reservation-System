package com.sz.reservation.registration.infrastructure.adapter.outbound;

import com.github.f4b6a3.uuid.UuidCreator;
import com.sz.reservation.registration.application.useCase.AccountCreationData;
import com.sz.reservation.registration.domain.model.AccountVerificationToken;
import com.sz.reservation.registration.domain.model.PhoneNumber;
import com.sz.reservation.registration.domain.model.ProfilePicture;
import com.sz.reservation.registration.domain.service.HashingService;
import com.sz.reservation.registration.infrastructure.service.BCryptPasswordHashingService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@DisplayName("Testing Account db ")
@Disabled
class UserRegistrationMySqlDbTest {
    private static HikariDataSource dataSource;

    @BeforeAll
    public static void setDataSource(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/userTestDb");
        config.setUsername("root");
        config.setPassword("12345");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        HikariDataSource ds = new HikariDataSource(config);
        dataSource = ds;
    }

    @Test
    @Transactional
    public void insertingDuplicateUser(){
        UserRegistrationMySqlDb userRegistrationMySqlDb = new UserRegistrationMySqlDb(dataSource);

        String userId = UuidCreator.getTimeOrderedEpoch().toString();
        String userVerificationToken = UuidCreator.getTimeOrderedEpoch().toString();
        LocalDate expirationDate = LocalDate.now().plusDays(7);

        String phoneNumberId =  UuidCreator.getTimeOrderedEpoch().toString();
        PhoneNumber phoneNumber = new PhoneNumber(phoneNumberId,"+54","1111448899");

        HashingService hashingService = new BCryptPasswordHashingService();
        String password =hashingService.hash("ultrasafepassword");

        AccountCreationData accountCreationData = new AccountCreationData(
                userId,
                "wolf of wall street",
                "jordan",
                "belfort",
                "zanone.santinoet36@gmail.com",
                phoneNumber,
                LocalDate.now(),
                "Argentina",
                password,
                new ProfilePicture("C:\\Users\\losmelli\\Pictures\\pfp_2025-02-03T18-22-31-bb7c3d7b-656d-409a-ada7-f204b8933074.jpg"),
                new AccountVerificationToken(userVerificationToken,expirationDate));

        userRegistrationMySqlDb.registerNotEnabledUser(accountCreationData);
    }


}