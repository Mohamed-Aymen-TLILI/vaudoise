package com.test.vaudoise.client.model;

import com.test.vaudoise.core.exception.ValidationException;
import com.test.vaudoise.domain.model.client.Name;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class NameTest {

    @Test
    void name_ok() {
        assertThat(new Name("Vaudoise").value()).isEqualTo("Vaudoise");
    }

    @Test
    void name_too_short() {
        assertThatThrownBy(() -> new Name("A"))
                .isInstanceOf(ValidationException.class)
                .hasMessage("name length must be between 2 and 200 characters");
    }

    @Test
    void null_name_not_ok() {
        assertThatThrownBy(() -> new Name(null)).isInstanceOf(ValidationException.class);
    }
}
