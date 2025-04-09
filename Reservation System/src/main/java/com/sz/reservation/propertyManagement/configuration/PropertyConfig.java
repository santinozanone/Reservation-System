package com.sz.reservation.propertyManagement.configuration;

import com.sz.reservation.propertyManagement.configuration.security.ListingWebSecurity;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableWebSecurity
@Import(ListingWebSecurity.class)
@ComponentScan("com.sz.reservation.propertyManagement")
public class PropertyConfig {
}
