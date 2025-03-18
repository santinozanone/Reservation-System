package com.sz.reservation.accountManagement.infrastructure.adapter.outbound;

import com.sz.reservation.accountManagement.domain.model.AccountVerificationToken;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountVerificationTokenRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Repository
public class AccountVerificationTokenMySql implements AccountVerificationTokenRepository {
    private JdbcTemplate jdbcTemplate;

    private static final Marker SQL_MARKER = MarkerManager.getMarker("SQL");
    private static final Marker INSERT_MARKER = MarkerManager.getMarker("SQL_INSERT").setParents(SQL_MARKER);
    private static final Marker QUERY_MARKER = MarkerManager.getMarker("SQL_QUERY").setParents(SQL_MARKER);

    private Logger logger = LogManager.getLogger(AccountVerificationTokenMySql.class);

    @Autowired
    public AccountVerificationTokenMySql(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Optional<AccountVerificationToken> findByToken(String token) {
        if (token == null || token.isEmpty()) throw new IllegalArgumentException("token cannot be null or empty");
        logger.debug(QUERY_MARKER, "executing select for token : {}", token);
        AccountVerificationToken accountVerificationToken;
        String sql = "Select BIN_TO_UUID(account_id),BIN_TO_UUID(token),expires_at from verification_token where token = UUID_TO_BIN(?)";
        accountVerificationToken = jdbcTemplate.query(sql,new ResultSetExtractor<AccountVerificationToken>(){
            @Override
            public AccountVerificationToken extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                if (!resultSet.next()) return null;
                return new AccountVerificationToken(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getDate(3).toLocalDate()
                );
            }
        },token);
        return Optional.ofNullable(accountVerificationToken);
    }

    @Override
    public void save(AccountVerificationToken accountVerificationToken) {
        if (accountVerificationToken == null)throw new IllegalArgumentException("account verification token cannot be null");
        logger.debug(INSERT_MARKER, "executing verification token insert: {}", accountVerificationToken.getToken());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String createdAt = dateFormatter.format(LocalDate.now());
        String expirationDate = dateFormatter.format(accountVerificationToken.getExpirationDate());
        String sql = "insert into verification_token (token,created_at,expires_at,account_id) VALUES" +
                "(UUID_TO_BIN(?),?,?,UUID_TO_BIN(?))";
        jdbcTemplate.update(sql,accountVerificationToken.getToken(),createdAt,expirationDate,accountVerificationToken.getUserId());
    }

    @Override
    public void update(String oldToken, AccountVerificationToken newToken) {
        if (oldToken == null || oldToken.isEmpty() || newToken == null )throw new IllegalArgumentException("old token or new token cannot be null or empty");
        logger.debug(INSERT_MARKER, "executing verification token update, old token: {}", oldToken);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String createdAt = dateFormatter.format(LocalDate.now());
        String expirationDate = dateFormatter.format(newToken.getExpirationDate());
        String sql = "update verification_token set token = UUID_TO_BIN(?), created_at = ?, expires_at = ?, account_id = UUID_TO_BIN(?) where token = UUID_TO_BIN(?)";
        jdbcTemplate.update(sql,newToken.getToken(),createdAt,expirationDate,newToken.getUserId(),oldToken);
    }
}
