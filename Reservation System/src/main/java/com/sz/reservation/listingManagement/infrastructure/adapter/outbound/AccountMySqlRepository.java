package com.sz.reservation.listingManagement.infrastructure.adapter.outbound;

import com.sz.reservation.listingManagement.domain.Account;
import com.sz.reservation.listingManagement.domain.port.outbound.AccountRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class AccountMySqlRepository implements AccountRepository {
    private JdbcTemplate jdbcTemplate;

    public AccountMySqlRepository(@Qualifier("listing.jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Account account) {
        String sql = "INSERT INTO account (id_account, username, name, surname, email,enabled) values (UUID_TO_BIN(?),?,?,?,?,?)";
        jdbcTemplate.update(sql,account.getId(),account.getUsername(),account.getName(),
                account.getSurname(),account.getEmail(),account.isEnabled());
    }

    @Override
    public void update(Account account) {
        String sql = "update account set username = ?, name = ?, surname = ?,enabled = ?";
        jdbcTemplate.update(sql,account.getUsername(),account.getName(), account.getSurname(),account.isEnabled());
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        String sql = "select BIN_TO_UUID(id_account) as id, username, name, surname, email, enabled from account where email = ?";
        Account account = jdbcTemplate.query(sql, new ResultSetExtractor<Account>() {
            @Override
            public Account extractData(ResultSet rs) throws SQLException, DataAccessException {
                Account account = null;
                while (rs.next()){
                    return new Account
                            (rs.getString("id"),
                            rs.getString("username"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("email"),
                            rs.getBoolean("enabled"));
                }
                return account;
            }
        },email);
        return Optional.ofNullable(account);
    }

    @Override
    public Optional<Account> findById(String id) {
        String sql = "select BIN_TO_UUID(id_account) as id, username, name, surname, email, enabled from account where id_account = UUID_TO_BIN(?)";
        Account account = jdbcTemplate.query(sql, new ResultSetExtractor<Account>() {
            @Override
            public Account extractData(ResultSet rs) throws SQLException, DataAccessException {
                Account account = null;
                while (rs.next()){
                    return new Account
                            (rs.getString("id"),
                                    rs.getString("username"),
                                    rs.getString("name"),
                                    rs.getString("surname"),
                                    rs.getString("email"),
                                    rs.getBoolean("enabled"));
                }
                return account;
            }
        },id);
        return Optional.ofNullable(account);
    }
}
