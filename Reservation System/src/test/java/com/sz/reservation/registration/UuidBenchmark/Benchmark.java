package com.sz.reservation.registration.UuidBenchmark;

import com.github.f4b6a3.uuid.UuidCreator;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Disabled
public class Benchmark {
    private static HikariDataSource dataSource;
    private final int NUMBER_INSERTS = 100000;


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
    public void benchmark_V7_UUID(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        long start = System.nanoTime();
        String sql = "INSERT INTO user (iduser, username, surname, uniqueEmail, profilePicturePath, " +
                "password, nationality, birthDate, countrycode, phoneNumber) " +
                "VALUES (UUID_TO_BIN(?), ?, ?, ?, ?, ?, ?, ?, ?, ?)";

     /*  //NOT BATCH
      for (int i = 0; i < 10000; i++) {
            String uuid = UuidCreator.getTimeOrderedEpoch().toString();
            jdbcTemplate.update(sql, uuid,
                    "john_doe",
                    "Doe",
                    "john.doe@example.com",
                    "/images/profiles/john.jpg",
                    "password123",
                    "American",
                    "1990-01-01",
                    "+1",
                    "1234567890");
        }*/

         //BATCH
        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 0; i < NUMBER_INSERTS; i++) {
            String uuid = UuidCreator.getTimeOrderedEpoch().toString();
            batchArgs.add(new Object[]{
                    uuid,
                    "john_doe",
                    "Doe",
                    "john.doe@example.com",
                    "/images/profiles/john.jpg",
                    "password123",
                    "American",
                    "1990-01-01",
                    "+1",
                    "1234567890"
            });
            // Execute batch and clear list after each batch size is reached
            if (batchArgs.size() == 700 || i == NUMBER_INSERTS - 1) { // update when batch size of 2000 o when loop is at the end

                jdbcTemplate.batchUpdate(sql, batchArgs);
                batchArgs.clear(); // Clear the batch after each insertion
            }

        }


       // jdbcTemplate.batchUpdate(sql,batchArgs);
        long end = System.nanoTime();
        System.out.println("it took " + (end - start));
    }
}
