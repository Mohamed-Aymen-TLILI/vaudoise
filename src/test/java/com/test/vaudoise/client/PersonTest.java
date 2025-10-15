package com.test.vaudoise.client;

import com.test.vaudoise.core.exception.ValidationException;
import com.test.vaudoise.domain.model.*;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class PersonTest {

    @Test
    void should_create_valid_person() {
        var person = new Person(
                new ClientId(UUID.randomUUID()),
                new Name("Mohamed Aymen TLILI"),
                new Email("matlili@example.com"),
                new Phone("+41791234567"),
                LocalDate.of(1990, 1, 1)
        );

        assertThat(person.name().value()).isEqualTo("Mohamed Aymen TLILI");
        assertThat(person.email().value()).isEqualTo("matlili@example.com");
        assertThat(person.phone().value()).isEqualTo("+41791234567");
        assertThat(person.birthdate()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(person.type()).isEqualTo(ClientType.PERSON);
    }

    @Test
    void should_throw_when_name_invalid() {
        assertThatThrownBy(() ->
                new Person(
                        new ClientId(UUID.randomUUID()),
                        new Name(""),
                        new Email("a@b.com"),
                        new Phone("+41791234567"),
                        LocalDate.now()
                )
        ).isInstanceOf(ValidationException.class);
    }

    @Test
    void should_throw_exception_when_birthdate_is_null() {
        assertThatThrownBy(() ->
                new Person(
                        new ClientId(UUID.randomUUID()),
                        new Name("Mohamed Aymen TLILI"),
                        new Email("matlili@example.com"),
                        new Phone("+41791234567"),
                        null
                )
        ).isInstanceOf(ValidationException.class)
                .hasMessage("Birthdate is null");
    }

    @Test
    void should_throw_exception_when_birthdate_in_future() {
        assertThatThrownBy(() ->
                new Person(
                        new ClientId(UUID.randomUUID()),
                        new Name("Mohamed Aymen TLILI"),
                        new Email("matlili@example.com"),
                        new Phone("+41791234567"),
                        LocalDate.now().plusDays(1)
                )
        ).isInstanceOf(ValidationException.class)
                .hasMessage("Birthdate cannot be in the future");
    }
}
