package com.sz.reservation.registration.infrastructure.adapter.outbound;

import com.sz.reservation.registration.application.useCase.AccountCreationData;
import com.sz.reservation.registration.domain.exception.EmailAlreadyRegisteredException;
import com.sz.reservation.registration.domain.exception.UsernameAlreadyRegisteredException;
import com.sz.reservation.registration.domain.port.outbound.UserRegistrationDb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Repository
public class UserRegistrationMySqlDb implements UserRegistrationDb {
    private JdbcTemplate jdbcTemplate;

    private Logger logger = LogManager.getLogger(UserRegistrationMySqlDb.class);
    private static final Marker SQL_MARKER = MarkerManager.getMarker("SQL");
    private static final Marker INSERT_MARKER = MarkerManager.getMarker("SQL_INSERT").setParents(SQL_MARKER);
    private static final Marker UPDATE_MARKER = MarkerManager.getMarker("SQL_UPDATE").setParents(SQL_MARKER);
    private static final Marker QUERY_MARKER = MarkerManager.getMarker("SQL_QUERY").setParents(SQL_MARKER);

    private final String USERNAME_FIELD_SQL_ERROR_MESSAGE = "username'";
    private final String EMAIL_FIELD_SQL_ERROR_MESSAGE = "email'";

    public UserRegistrationMySqlDb(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public void registerNotEnabledUser(AccountCreationData accountCreationData) {
        logger.debug("registering not enabled user with email: {} , in MySql db ",accountCreationData.getEmail());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formattedBirthDate = dateFormatter.format(accountCreationData.getBirthDate());
        LocalDateTime localDateTime = LocalDateTime.now();
        String currentDate = dateTimeFormatter.format(localDateTime);

        String phoneNumberSql = "INSERT INTO phone_number(idphoneNumber,countryCode,phoneNumber) VALUES (UUID_TO_BIN(?),?,?)";
        String accountSql = "INSERT INTO account (iduser, username, name, surname, email, birthDate, password, profilePicturePath, phoneNumberId,createdAt,enabled) " +
                "VALUES (UUID_TO_BIN(?), ?, ?, ?, ?, ?, ?, ?, UUID_TO_BIN(?), ?,?)";
        String verificationTokenSql = "INSERT INTO verification_token(token,created_at,expires_at,account_id) VALUES (UUID_TO_BIN(?),?,?,UUID_TO_BIN(?))";


        //Inserting phone number
        String phoneId = accountCreationData.getPhoneNumber().getId();
        String countryCode = accountCreationData.getPhoneNumber().getCountryCode();
        String phoneNumber = accountCreationData.getPhoneNumber().getPhoneNumber();
        logger.debug(INSERT_MARKER,"executing phone number insertion: {}",phoneNumberSql);
        jdbcTemplate.update(phoneNumberSql, phoneId, countryCode, phoneNumber);

        //Inserting account
        String profilePicturePath = accountCreationData.getProfilePicture().getImagePath();
        logger.debug("executing account data insertion: {}",accountSql);
        try {
            jdbcTemplate.update(accountSql, accountCreationData.getId(), accountCreationData.getUsername(), accountCreationData.getName(),
                    accountCreationData.getSurname(), accountCreationData.getEmail(), formattedBirthDate, accountCreationData.getPassword(),
                    profilePicturePath, phoneId, currentDate, true);
        }catch (DuplicateKeyException exception){
            logger.info("error trying to insert user with uuid:{}, username:{}, email:{}, duplicate key exception",
                    accountCreationData.getId(), accountCreationData.getUsername(),accountCreationData.getEmail());
            handleSqlIntegrityException(exception);
        }
        logger.debug(INSERT_MARKER,"executing verification token insert: {}",verificationTokenSql);
        String expires_at = dateFormatter.format(accountCreationData.getVerificationToken().getExpirationDate());
        jdbcTemplate.update(verificationTokenSql, accountCreationData.getVerificationToken().getToken(), dateFormatter.format(localDateTime), expires_at, accountCreationData.getId());
        logger.info("Successfully inserted user with uuid:{}, username:{}, email:{}",accountCreationData.getId(), accountCreationData.getUsername(),accountCreationData.getEmail());

    }
    public void handleSqlIntegrityException(DuplicateKeyException exception) {
        String errorMessage = exception.getRootCause().getMessage();
        if (errorMessage.endsWith(EMAIL_FIELD_SQL_ERROR_MESSAGE)){
            logger.info("error trying to insert user, email already in use");
            throw new EmailAlreadyRegisteredException(exception);
        }
        if (errorMessage.endsWith(USERNAME_FIELD_SQL_ERROR_MESSAGE)){
            logger.info("error trying to insert user, username already in use");
            throw new UsernameAlreadyRegisteredException(exception);
        }
    }



}
