package com.test.vaudoise.domain.model;

import com.test.vaudoise.core.exception.ValidationException;

import java.time.LocalDate;

public record Person(ClientId id, Name name, Email email, Phone phone, LocalDate birthdate) implements Client {
    public Person {
        if (birthdate == null) throw new ValidationException("Birthdate is null");
        if (birthdate.isAfter(LocalDate.now()))
            throw new ValidationException("Birthdate cannot be in the future");
    }
    @Override
    public ClientType type() {
        return ClientType.PERSON;
    }

    @Override
    public Client update(Name name, Email email, Phone phone) {
        return new Person(this.id, name, email, phone, this.birthdate);
    }
}
