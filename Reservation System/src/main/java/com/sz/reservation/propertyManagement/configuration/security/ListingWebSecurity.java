package com.sz.reservation.propertyManagement.configuration.security;

import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.globalConfiguration.security.userDetails.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class ListingWebSecurity {
    private final String SERVLET_PATH = "/api/v1/host";

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector).servletPath(SERVLET_PATH);
    }

    @Bean(value = "springSecurityFilterChain")
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                                .requestMatchers(mvc.pattern("/listings/**")).permitAll()
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
              //  .exceptionHandling(handlingConfigurer ->
                //        handlingConfigurer.accessDeniedHandler(accessDeniedHandler))
                .httpBasic(Customizer.withDefaults())
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
