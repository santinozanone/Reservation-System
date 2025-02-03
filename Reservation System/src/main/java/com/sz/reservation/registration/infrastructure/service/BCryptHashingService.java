package com.sz.reservation.registration.infrastructure.service;

import com.sz.reservation.registration.application.useCase.HashingService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptHashingService implements HashingService {
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String hash(String stringToHash) {
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(stringToHash);
    }
}
