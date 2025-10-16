package com.test.vaudoise.domain.model;

public record Company(ClientId id, Name name, Email email, Phone phone, CompanyIdentifier companyIdentifier) implements Client {
    @Override
    public ClientType type() {
        return ClientType.COMPANY;
    }

    @Override
    public Client update(Name name, Email email, Phone phone) {
        return new Company(this.id, name, email, phone, this.companyIdentifier);
    }
}
