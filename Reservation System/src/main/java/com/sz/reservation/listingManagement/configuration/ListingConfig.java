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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
//@EnableWebMvc
@EnableAutoConfiguration
@PropertySource("classpath:listing.properties")
@ComponentScan("com.sz.reservation.listingManagement")
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@EnableLoadTimeWeaving
public class ListingConfig {

    @Bean
    public ListingPropertyUseCase listingPropertyUseCase(ListingPropertyRepository listingPropertyRepository,
                                                         TikaListingImageValidator tikaListingImageValidator, ListingImageStorage imageStorage,
                                                         ListingImageMetadataRepository listingImageMetadataRepository){
        return new ListingPropertyUseCase(listingPropertyRepository,tikaListingImageValidator,imageStorage,listingImageMetadataRepository);
    }



    @Bean
    @Profile("test")
    public DataSource testHikariCP(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/usertestdb");
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
    @Profile("prod")
    public DataSource hikariCP(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/userDb");
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
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public FileTypeValidator tikaFileValidator(){
        return new TikaFileValidator();
    }

}
