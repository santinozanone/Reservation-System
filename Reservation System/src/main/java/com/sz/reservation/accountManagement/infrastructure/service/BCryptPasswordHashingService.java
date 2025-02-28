package com.sz.reservation.accountManagement.infrastructure.service;

import com.sz.reservation.accountManagement.domain.service.HashingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordHashingService implements HashingService {
    private Logger logger = LogManager.getLogger(BCryptPasswordHashingService.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String hash(String stringToHash) {
        logger.debug("hashing password ");
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(stringToHash);
    }
}
