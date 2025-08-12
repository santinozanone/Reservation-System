package com.sz.reservation.accountManagement.configuration;

import com.sz.reservation.accountManagement.application.service.AccountCreationService;
import com.sz.reservation.accountManagement.application.service.ProfilePictureService;
import com.sz.reservation.accountManagement.application.useCase.AccountRegistrationUseCase;
import com.sz.reservation.accountManagement.application.useCase.AccountVerificationUseCase;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountVerificationTokenRepository;
import com.sz.reservation.accountManagement.domain.port.outbound.ProfilePictureStorage;
import com.sz.reservation.accountManagement.domain.port.outbound.VerificationTokenEmailSender;
import com.sz.reservation.accountManagement.domain.service.HashingService;
import com.sz.reservation.accountManagement.domain.service.MultipartImageResizingService;
import com.sz.reservation.accountManagement.domain.service.PhoneNumberValidator;
import com.sz.reservation.accountManagement.domain.service.ProfilePictureTypeValidator;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.aspectj.AnnotationTransactionAspect;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.sql.DataSource;


@Configuration
@PropertySource("classpath:account.properties")
public class AccountConfig {
    @Value("${host}")
    private String host;

   @Bean(initMethod = "migrate")
    public Flyway accountMigration(){
       return Flyway.configure()
                .locations("classpath:db/migration/account")
                .dataSource(
                        "jdbc:mysql://"+host+":3306/accountBcTest",
                        "root",
                        "12345")
                .load();
    }



    @Bean("account.dataSource")
    @Profile("prod")
    public DataSource hikariCP(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://"+host+":3306/accountBc");
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

    @Bean("account.dataSource")
    @Profile("test")
    public DataSource testHikariCP(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://"+host+":3306/accountBcTest");
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

    @Bean("account.jdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("account.dataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean("account.transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("account.dataSource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public StandardServletMultipartResolver multipartResolver(){
        return new StandardServletMultipartResolver();
    }

    @Bean
    public ProfilePictureService profilePictureService(ProfilePictureStorage profilePictureStorage,
                                                       ProfilePictureTypeValidator profilePictureTypeValidator,
                                                       MultipartImageResizingService multipartImageResizingService){
        return new ProfilePictureService(profilePictureStorage,profilePictureTypeValidator,multipartImageResizingService);
    }
    @Bean
    public AccountVerificationUseCase accountVerificationUseCase(AccountRepository accountRepository,
                                                                 AccountVerificationTokenRepository verificationTokenRepository,
                                                                 VerificationTokenEmailSender verificationTokenEmailSender,
                                                                 ApplicationEventPublisher eventPublisher){
       return new AccountVerificationUseCase(accountRepository,verificationTokenRepository,verificationTokenEmailSender,eventPublisher);
    }

    @Bean
    public AccountCreationService accountCreation(PhoneNumberValidator phoneNumberValidator, HashingService hashingService){
        return new AccountCreationService(phoneNumberValidator, hashingService);
    }

    @Bean
    public AccountRegistrationUseCase registrationUseCase(AccountRepository accountRepository, AccountVerificationTokenRepository verificationTokenRepository,
                                                          ProfilePictureService profilePictureService, VerificationTokenEmailSender verificationTokenEmailSender,
                                                          AccountCreationService accountCreationService){
        return new AccountRegistrationUseCase(accountRepository,verificationTokenRepository,verificationTokenEmailSender,profilePictureService, accountCreationService);
    }
}
