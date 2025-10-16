package com.test.vaudoise.client.model;

import com.test.vaudoise.core.exception.ValidationException;
import com.test.vaudoise.domain.model.client.CompanyIdentifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class CompanyIdentifierTest {

    @ParameterizedTest
    @ValueSource(strings = {"aaa-111", "BBB-123", "ABC-123", "abc-987"})
    void company_identifier_ok(String p) {
        assertThatNoException().isThrownBy(() -> new CompanyIdentifier(p));
    }

    @Test
    void company_identifier_not_ok() {
        assertThatThrownBy(() -> new CompanyIdentifier("qqqq-test"))
                .isInstanceOf(ValidationException.class)
                .hasMessage("invalid companyIdentifier");
    }

    @Test
    void company_identifier_not_ok_() {
        assertThatThrownBy(() -> new CompanyIdentifier("123-abc"))
                .isInstanceOf(ValidationException.class)
                .hasMessage("invalid companyIdentifier");
    }

    @Test
    void null_company_identifier_not_ok() {
        assertThatThrownBy(() -> new CompanyIdentifier(null))
                .isInstanceOf(ValidationException.class)
                .hasMessage("invalid companyIdentifier");
    }
}
