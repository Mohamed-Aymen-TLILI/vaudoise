package com.test.vaudoise.domain.model;

public  sealed interface Client  permits Person, Company {
    ClientId id ();
    ClientType type ();
    Client update(Name name, Email email, Phone phone);
}
