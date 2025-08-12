package com.sz.reservation.listingManagement.configuration;

import com.sz.reservation.listingManagement.application.useCase.listing.ListingPropertyUseCase;
import com.sz.reservation.listingManagement.domain.port.outbound.ListingImageMetadataRepository;
import com.sz.reservation.listingManagement.domain.port.outbound.ListingImageStorage;
import com.sz.reservation.listingManagement.domain.port.outbound.ListingPropertyRepository;
import com.sz.reservation.listingManagement.infrastructure.service.TikaListingImageValidator;
import com.sz.reservation.util.FileTypeValidator;
import com.sz.reservation.util.TikaFileValidator;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:listing.properties")
public class ListingConfig {

    @Value("${host}")
    private String host;

    @Bean(initMethod = "migrate")
    public Flyway listingMigration(){
        return Flyway.configure()
                .locations("classpath:db/migration/listing")
                .dataSource(
                        "jdbc:mysql://"+host+":3306/listingBcTest",
                        "root",
                        "12345")
                .load();
    }

    @Bean
    public ListingPropertyUseCase listingPropertyUseCase(ListingPropertyRepository listingPropertyRepository,
                                                         TikaListingImageValidator tikaListingImageValidator, ListingImageStorage imageStorage,
                                                         ListingImageMetadataRepository listingImageMetadataRepository){
        return new ListingPropertyUseCase(listingPropertyRepository,tikaListingImageValidator,imageStorage,listingImageMetadataRepository);
    }



    @Bean("listing.dataSource")
    @Profile("test")
    public DataSource testHikariCP(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://"+host+":3306/listingBcTest");
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

    @Bean("listing.dataSource")
    @Profile("prod")
    public DataSource hikariCP(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://"+host+":3306/listingBc");
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


    @Bean("listing.jdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("listing.dataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean("listing.transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("listing.dataSource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

}
