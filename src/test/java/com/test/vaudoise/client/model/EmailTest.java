package com.test.vaudoise.client.model;

import com.test.vaudoise.core.exception.ValidationException;
import com.test.vaudoise.domain.model.Email;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class EmailTest {

    @Test
    void email_ok() {
        assertThat(new Email("Vaudoise@vaudoise.com").value()).isEqualTo("Vaudoise@vaudoise.com");
    }

    @Test
    void email_not_ok() {
        assertThatThrownBy(() -> new Email("bademail"))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Invalid email address");
    }

    @Test
    void null_email_not_ok() {
        assertThatThrownBy(() -> new Email(null))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Invalid email address");
    }
}
