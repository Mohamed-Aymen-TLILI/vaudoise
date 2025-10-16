package com.test.vaudoise.infrastructure.web.dto.client;

import jakarta.validation.constraints.Email;

public record CreateCompanyRequest(
        String name,
        @Email String email,
        String phone,
        String companyIdentifier
){}
