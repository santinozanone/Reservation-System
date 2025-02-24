package com.sz.reservation.registration.infrastructure.adapter.outbound;

import com.sz.reservation.registration.application.useCase.AccountCreationData;
import com.sz.reservation.registration.domain.exception.EmailAlreadyRegisteredException;
import com.sz.reservation.registration.domain.exception.UsernameAlreadyRegisteredException;
import com.sz.reservation.registration.domain.model.Account;
import com.sz.reservation.registration.domain.model.PhoneNumber;
import com.sz.reservation.registration.domain.model.ProfilePicture;
import com.sz.reservation.registration.domain.port.outbound.AccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Repository
public class AccountRepositoryMySql implements AccountRepository {
    private JdbcTemplate jdbcTemplate;

    private Logger logger = LogManager.getLogger(AccountRepositoryMySql.class);
    private static final Marker SQL_MARKER = MarkerManager.getMarker("SQL");
    private static final Marker INSERT_MARKER = MarkerManager.getMarker("SQL_INSERT").setParents(SQL_MARKER);
    private static final Marker UPDATE_MARKER = MarkerManager.getMarker("SQL_UPDATE").setParents(SQL_MARKER);
    private static final Marker QUERY_MARKER = MarkerManager.getMarker("SQL_QUERY").setParents(SQL_MARKER);

    private final String USERNAME_FIELD_SQL_ERROR_MESSAGE = "username'";
    private final String EMAIL_FIELD_SQL_ERROR_MESSAGE = "email'";

    public AccountRepositoryMySql(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public Optional<Account> findAccountByUsername(String username) {
        if (username == null)throw new IllegalArgumentException("username cannot be null");
        logger.debug(QUERY_MARKER, "finding account with username: {} ", username);
        String accountQuery = "select BIN_TO_UUID(iduser) as iduser,ac.username,ac.name,ac.surname,ac.email,BIN_TO_UUID(ph.idphoneNumber) as phoneId,ph.countryCode,ph.phoneNumber,ac.profilePicturePath,ac.password,ac.verified,ac.enabled from account " +
                "as ac inner join phone_number as ph on ac.phoneNumberId = ph.idphoneNumber where ac.username = ?";
        logger.debug(QUERY_MARKER, "executing query: {} ", accountQuery);

        Account account = jdbcTemplate.query(
                accountQuery,
                new ResultSetExtractor<Account>() {
                    @Override
                    public Account extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                        if (!resultSet.next()) return null;
                        return new Account(resultSet.getString("iduser"), resultSet.getString("username"), resultSet.getString("name"), resultSet.getString("surname"),
                                resultSet.getString("email"), new PhoneNumber(resultSet.getString("phoneId"), resultSet.getString("countryCode"), resultSet.getString("phoneNumber")),
                                new ProfilePicture(resultSet.getString("profilePicturePath")), resultSet.getString("password"),resultSet.getBoolean("verified"), resultSet.getBoolean("enabled"));
                    }
                }, username);

        return Optional.ofNullable(account);
    }

    @Override
    public Optional<Account> findAccountByEmail(String email) {
        if (email == null)throw new IllegalArgumentException("email cannot be null");
        logger.debug(QUERY_MARKER, "finding account with email: {} ", email);
        String accountQuery = "select BIN_TO_UUID(iduser) as iduser,ac.username,ac.name,ac.surname,ac.email,BIN_TO_UUID(ph.idphoneNumber) as phoneId,ph.countryCode,ph.phoneNumber,ac.profilePicturePath,ac.password,ac.verified,ac.enabled from account " +
                "as ac inner join phone_number as ph on ac.phoneNumberId = ph.idphoneNumber where ac.email = ?";
        logger.debug(QUERY_MARKER, "executing query: {} ", accountQuery);

        Account account = jdbcTemplate.query(
                accountQuery,
                new ResultSetExtractor<Account>() {
                    @Override
                    public Account extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                        if (!resultSet.next()) return null;
                        return new Account(resultSet.getString("iduser"), resultSet.getString("username"), resultSet.getString("name"), resultSet.getString("surname"),
                                resultSet.getString("email"), new PhoneNumber(resultSet.getString("phoneId"), resultSet.getString("countryCode"), resultSet.getString("phoneNumber")),
                                new ProfilePicture(resultSet.getString("profilePicturePath")), resultSet.getString("password"),resultSet.getBoolean("verified") ,resultSet.getBoolean("enabled"));
                    }
                }, email);
        return Optional.ofNullable(account);
    }


    @Override
    public void registerNotEnabledNotVerifiedUser(AccountCreationData accountCreationData) {
        if (accountCreationData == null)throw new IllegalArgumentException("accountCreationData cannot be null");
        logger.debug("registering not enabled user with email: {} , in MySql db ", accountCreationData.getEmail());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formattedBirthDate = dateFormatter.format(accountCreationData.getBirthDate());
        LocalDateTime localDateTime = LocalDateTime.now();
        String currentDate = dateTimeFormatter.format(localDateTime);

        String phoneNumberSql = "INSERT INTO phone_number(idphoneNumber,countryCode,phoneNumber) VALUES (UUID_TO_BIN(?),?,?)";
        String accountSql = "INSERT INTO account (iduser, username, name, surname, email, birthDate, password, profilePicturePath, phoneNumberId,createdAt,verified,enabled) " +
                "VALUES (UUID_TO_BIN(?), ?, ?, ?, ?, ?, ?, ?, UUID_TO_BIN(?), ?,?,?)";
        String verificationTokenSql = "INSERT INTO verification_token(token,created_at,expires_at,account_id) VALUES (UUID_TO_BIN(?),?,?,UUID_TO_BIN(?))";


        //Inserting phone number
        String phoneId = accountCreationData.getPhoneNumber().getId();
        String countryCode = accountCreationData.getPhoneNumber().getCountryCode();
        String phoneNumber = accountCreationData.getPhoneNumber().getPhoneNumber();
        logger.debug(INSERT_MARKER, "executing phone number insertion: {}", phoneNumberSql);
        jdbcTemplate.update(phoneNumberSql, phoneId, countryCode, phoneNumber);

        //Inserting account
        boolean verified = false;
        boolean enabled = true;
        String profilePicturePath = accountCreationData.getProfilePicture().getImagePath();
        logger.debug("executing account data insertion: {}", accountSql);
        try {
            jdbcTemplate.update(accountSql, accountCreationData.getId(), accountCreationData.getUsername(), accountCreationData.getName(),
                    accountCreationData.getSurname(), accountCreationData.getEmail(), formattedBirthDate, accountCreationData.getPassword(),
                    profilePicturePath, phoneId, currentDate,verified ,enabled);
        } catch (DuplicateKeyException exception) {
            logger.info("error trying to insert user with uuid:{}, username:{}, email:{}, duplicate key exception",
                    accountCreationData.getId(), accountCreationData.getUsername(), accountCreationData.getEmail());
            handleSqlIntegrityException(accountCreationData.getEmail(), accountCreationData.getUsername(), exception);
        }
        logger.debug(INSERT_MARKER, "executing verification token insert: {}", verificationTokenSql);
        String expires_at = dateFormatter.format(accountCreationData.getVerificationToken().getExpirationDate());
        jdbcTemplate.update(verificationTokenSql, accountCreationData.getVerificationToken().getToken(), dateFormatter.format(localDateTime), expires_at, accountCreationData.getId());
        logger.info("Successfully inserted user with uuid:{}, username:{}, email:{}", accountCreationData.getId(), accountCreationData.getUsername(), accountCreationData.getEmail());

    }


    private void handleSqlIntegrityException(String email, String username, DuplicateKeyException exception) {
        String errorMessage = exception.getRootCause().getMessage();
        if (errorMessage.endsWith(EMAIL_FIELD_SQL_ERROR_MESSAGE)) {
            throw new EmailAlreadyRegisteredException(email, exception);
        }
        if (errorMessage.endsWith(USERNAME_FIELD_SQL_ERROR_MESSAGE)) {
            throw new UsernameAlreadyRegisteredException(username, exception);
        }
    }


}
