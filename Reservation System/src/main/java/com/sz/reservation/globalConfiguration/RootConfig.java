package com.sz.reservation.globalConfiguration;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "com.sz.reservation.util")
//@EnableWebMvc
//@EnableAutoConfiguration
//@PropertySource("classpath:application.properties")
//@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
//@EnableLoadTimeWeaving
public class RootConfig /*implements WebMvcConfigurer */{


 /*   @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        WebMvcConfigurer.super.extendMessageConverters(converters);
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter ) {
                ObjectMapper mapper = ((MappingJackson2HttpMessageConverter) converter).getObjectMapper();
                // Register the ParameterNamesModule to support constructor-based deserialization
                mapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
                mapper.registerModule(new JavaTimeModule());
            }
        }
    }

    @Bean
    public static MethodValidationPostProcessor validationPostProcessor() {
        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        processor.setAdaptConstraintViolations(true);
        return processor;
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

*/


  /*  @Bean
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
    public AccountRepository accountRepository(JdbcTemplate jdbcTemplate){
        return new AccountRepositoryMySql(jdbcTemplate);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
*/


}
