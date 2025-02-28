package com.sz.reservation.accountManagement.application.useCase;

import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;

public class AccountVerificationUseCase {
    private AccountRepository repository;

    public AccountVerificationUseCase(AccountRepository repository) {
        this.repository = repository;
    }

    public void verifyAccount(String token){

    }
}
