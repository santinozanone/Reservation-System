package com.sz.reservation.accountManagement.infrastructure.adapter.outbound;

import com.sz.reservation.accountManagement.domain.model.AccountVerificationToken;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountVerificationTokenRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class AccountVerificationTokenMySql implements AccountVerificationTokenRepository {
    private JdbcTemplate jdbcTemplate;
    private Logger logger = LogManager.getLogger(AccountVerificationTokenMySql.class);

    @Autowired
    public AccountVerificationTokenMySql(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Optional<AccountVerificationToken> findByToken(String token) {
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

    }
}
