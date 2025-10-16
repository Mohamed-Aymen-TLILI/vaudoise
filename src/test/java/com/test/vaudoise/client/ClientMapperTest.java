package com.test.vaudoise.client;

import com.test.vaudoise.domain.model.client.*;
import com.test.vaudoise.infrastructure.web.dto.client.ClientType;
import com.test.vaudoise.infrastructure.web.mapper.client.ClientMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ClientMapperTest {

    @Test
    void should_map_person_to_response() {
        var person = new Person(
                new ClientId(UUID.randomUUID()),
                new Name("John Doe"),
                new Email("john@vaudoise.ch"),
                new Phone("+41791234567"),
                LocalDate.of(1990, 5, 15)
        );

        var response = ClientMapper.toResponse(person);

        assertThat(response.type()).isEqualTo(ClientType.PERSON);
        assertThat(response.name()).isEqualTo("John Doe");
        assertThat(response.birthdate()).isEqualTo(LocalDate.of(1990, 5, 15));
        assertThat(response.companyIdentifier()).isNull();
    }

    @Test
    void should_map_company_to_response() {
        var company = new Company(
                new ClientId(UUID.randomUUID()),
                new Name("Vaudoise SA"),
                new Email("info@vaudoise.ch"),
                new Phone("+41791234567"),
                new CompanyIdentifier("vaa-123")
        );

        var response = ClientMapper.toResponse(company);

        assertThat(response.type()).isEqualTo(ClientType.COMPANY);
        assertThat(response.companyIdentifier()).isEqualTo("vaa-123");
        assertThat(response.birthdate()).isNull();
    }
}
