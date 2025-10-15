package com.test.vaudoise.client.model;

import com.test.vaudoise.core.exception.ValidationException;
import com.test.vaudoise.domain.model.Phone;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class PhoneTest {

    @ParameterizedTest
    @ValueSource(strings = {"+41791234567", "+41 79 123 45 67", "079 123 45 67", "0791234567", "0041791234567"})
    void phone_ok (String p) {
        assertThatNoException().isThrownBy(() -> new Phone(p));
    }

    @ParameterizedTest
    @ValueSource(strings = {"BAD", "++41", "123", "", "+41", "0041"})
    void phone_ko (String p) {
        assertThatThrownBy(() -> new Phone(p)).isInstanceOf(ValidationException.class);
    }

}
