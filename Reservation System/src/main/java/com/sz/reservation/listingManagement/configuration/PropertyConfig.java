package com.sz.reservation.listingManagement.configuration;

import com.sz.reservation.listingManagement.application.useCase.listing.ListingPropertyUseCase;
import com.sz.reservation.listingManagement.domain.port.outbound.ListingImageMetadataRepository;
import com.sz.reservation.listingManagement.domain.port.outbound.ListingImageStorage;
import com.sz.reservation.listingManagement.domain.port.outbound.ListingPropertyRepository;
import com.sz.reservation.listingManagement.infrastructure.service.TikaListingImageValidator;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@PropertySource("classpath:application.properties")
@ComponentScan("com.sz.reservation.propertyManagement")
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@EnableLoadTimeWeaving
public class PropertyConfig {

    @Bean
    public ListingPropertyUseCase listingPropertyUseCase(ListingPropertyRepository listingPropertyRepository,
                                                         TikaListingImageValidator tikaListingImageValidator, ListingImageStorage imageStorage,
                                                         ListingImageMetadataRepository listingImageMetadataRepository){
        return new ListingPropertyUseCase(listingPropertyRepository,tikaListingImageValidator,imageStorage,listingImageMetadataRepository);
    }


}
