package com.sz.reservation.configuration;

import com.sz.reservation.registration.application.useCase.*;
import com.sz.reservation.registration.domain.port.outbound.ProfilePictureStorage;
import com.sz.reservation.registration.domain.port.outbound.UserRegistrationDb;
import com.sz.reservation.registration.domain.port.outbound.VerificationTokenEmailSender;
import com.sz.reservation.registration.domain.service.*;
import com.sz.reservation.util.FileTypeValidator;
import com.sz.reservation.util.TikaFileValidator;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.sz.reservation")
@EnableWebMvc
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
public class RootConfig {

    @Bean
    public StandardServletMultipartResolver multipartResolver(){
        return new StandardServletMultipartResolver();
    }


    @Bean
    public FileTypeValidator tikaFileValidator(){
        return new TikaFileValidator();
    }

    @Bean
    public DataSource hikariCP(){
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
        return ds;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public AccountCreation accountCreation(PhoneNumberValidator phoneNumberValidator,HashingService hashingService){
        return new AccountCreation(phoneNumberValidator, hashingService);
    }

    @Bean
    public AccountRegistrationUseCase registrationUseCase(UserRegistrationDb userRegistrationDb, ProfilePictureStorage profilePictureStorage,
                                                          MultipartImageResizingService multipartImageResizingService, ProfilePictureTypeValidator profilePictureTypeValidator,
                                                          VerificationTokenEmailSender verificationTokenEmailSender, AccountCreation accountCreation){
        return new AccountRegistrationUseCase(userRegistrationDb, profilePictureStorage , multipartImageResizingService, profilePictureTypeValidator,verificationTokenEmailSender,accountCreation);
    }
}
