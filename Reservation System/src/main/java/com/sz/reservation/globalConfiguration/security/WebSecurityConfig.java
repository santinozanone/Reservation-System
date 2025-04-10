package com.sz.reservation.globalConfiguration.security;

import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.globalConfiguration.security.userDetails.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity(debug = true)
//@ComponentScan
public class WebSecurityConfig {

   /* @Autowired
    @Qualifier("CustomAuthenticationEntryPoint")
    private AuthenticationEntryPoint authEntryPoint;

    @Autowired
    @Qualifier("CustomAccessDeniedHandler")
    private AccessDeniedHandler accessDeniedHandler;
*/

    //maybe i should have 3 dispatcher servlets now that i think because i have errors othertwise,
    // should have / , /api/v1/account/listing, /api/v1/account/*

   /* @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }
*/

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //mvc.servletPath("/api/v1");
        http.securityMatcher("/api/v1/account/*")
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().permitAll()
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
              //  .exceptionHandling(handlingConfigurer ->
                //        handlingConfigurer.accessDeniedHandler(accessDeniedHandler))
                //.httpBasic(Customizer.withDefaults()
                 //       basicConfigurer.authenticationEntryPoint(authEntryPoint)
                //)
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception {
       // mvc.servletPath("/api/v1/host");
        http.securityMatcher("/api/v1/host/*")
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().permitAll()
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //  .exceptionHandling(handlingConfigurer ->
                //        handlingConfigurer.accessDeniedHandler(accessDeniedHandler))
                //.httpBasic(Customizer.withDefaults()
                //       basicConfigurer.authenticationEntryPoint(authEntryPoint)
                //)
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }




    @Bean
    public UserDetailsService userDetailsService(AccountRepository accountRepository) {
        return new CustomUserDetailsService(accountRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



}
