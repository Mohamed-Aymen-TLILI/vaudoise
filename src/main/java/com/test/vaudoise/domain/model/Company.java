package com.test.vaudoise.domain.model;

public record Company(ClientId id, Name name, Email email, Phone phone, CompanyIdentifier companyIdentifier) implements Client {
    @Override
    public ClientType type() {
        return ClientType.COMPANY;
    }
}
