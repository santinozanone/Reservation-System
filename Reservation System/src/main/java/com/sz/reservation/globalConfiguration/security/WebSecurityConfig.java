package com.sz.reservation.globalConfiguration.security;

import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.globalConfiguration.security.userDetails.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig {

    @Autowired
    @Qualifier("CustomAuthenticationEntryPoint")
    private AuthenticationEntryPoint authEntryPoint;

    @Autowired
    @Qualifier("CustomAccessDeniedHandler")
    private AccessDeniedHandler accessDeniedHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/api/v1/account/registration").permitAll()
                        .requestMatchers("/api/v1/account/verification").permitAll()
                        .requestMatchers("/api/v1/account/verification/resend").permitAll()
                        .anyRequest().hasAuthority("ENABLED_USER")
                ).sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(handlingConfigurer -> handlingConfigurer.accessDeniedHandler(accessDeniedHandler))
                .httpBasic(basicConfigurer -> basicConfigurer.authenticationEntryPoint(authEntryPoint))
                .csrf(csrf -> csrf.disable());

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
