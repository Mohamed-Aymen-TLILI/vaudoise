package com.test.vaudoise.infrastructure.web.dto.client;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.UUID;

public record ClientResponse (
        UUID id, ClientType type, String name, String email, String phone,
        @JsonInclude(JsonInclude.Include.NON_NULL) LocalDate birthdate,
        @JsonInclude(JsonInclude.Include.NON_NULL) String companyIdentifier
){}