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

import java.util.Scanner;

//@Configuration
//@EnableWebSecurity(debug = true)
public class WebSecurityConfig {

    @Autowired
    @Qualifier("CustomAuthenticationEntryPoint")
    private AuthenticationEntryPoint authEntryPoint;

    @Autowired
    @Qualifier("CustomAccessDeniedHandler")
    private AccessDeniedHandler accessDeniedHandler;


    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        mvc.servletPath("/api/v1"); // defining the servlet path in the mvc Request Matcher
        http.securityMatcher(mvc.pattern("/**"))// we need to use the same mvc in the security matcher,so it uses the servlet path previously defined
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(mvc.pattern("/**")).permitAll())//mvc pattern again so it uses the servlet path
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler))
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }


    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain2(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        mvc.servletPath("/api/v1/host"); // defining the servlet path in the mvc Request Matcher
        http.securityMatcher(mvc.pattern("/**")) // we need to use the same mvc in the security matcher,so it uses the servlet path previously defined
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(mvc.pattern("/listing/**")).hasAuthority("ENABLED_VERIFIED_USER")) //mvc pattern again so it uses the servlet path
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handlingConfigurer ->
                        handlingConfigurer.accessDeniedHandler(accessDeniedHandler))
                .httpBasic(basicConfigurer ->
                        basicConfigurer.authenticationEntryPoint(authEntryPoint))
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }


    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public UserDetailsService userDetailsService(AccountRepository accountRepository) {
        return new CustomUserDetailsService(accountRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
