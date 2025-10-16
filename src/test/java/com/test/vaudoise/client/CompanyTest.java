package com.test.vaudoise.client;

import com.test.vaudoise.core.exception.ValidationException;
import com.test.vaudoise.domain.model.client.*;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class CompanyTest {

    @Test
    void should_create_valid_company() {
        var company = new Company(
                new ClientId(UUID.randomUUID()),
                new Name("Vaudoise Assurances"),
                new Email("contact@vaudoise.ch"),
                new Phone("+41761234567"),
                new CompanyIdentifier("vaa-123")
        );

        assertThat(company.name().value()).isEqualTo("Vaudoise Assurances");
        assertThat(company.companyIdentifier().value()).isEqualTo("vaa-123");
        assertThat(company.type()).isEqualTo(ClientType.COMPANY);
    }

    @Test
    void should_throw_when_identifier_invalid() {
        assertThatThrownBy(() ->
                new Company(
                        new ClientId(UUID.randomUUID()),
                        new Name("Test"),
                        new Email("contact@vaudoise.ch"),
                        new Phone("+41221234567"),
                        new CompanyIdentifier("INVALID!")
                )
        ).isInstanceOf(ValidationException.class);
    }
}
