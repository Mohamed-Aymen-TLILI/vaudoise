package com.test.vaudoise.domain.model.client;

public  sealed interface Client  permits Person, Company {
    ClientId id ();
    ClientType type ();
    Client update(Name name, Email email, Phone phone);
}
