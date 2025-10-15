package com.test.vaudoise.domain.model;

public  sealed interface Client  permits Person, Company {
    ClientId id ();
    ClientType type ();
}
