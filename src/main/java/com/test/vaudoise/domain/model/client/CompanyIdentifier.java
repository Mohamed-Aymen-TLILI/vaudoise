package com.test.vaudoise.domain.model.client;

import com.test.vaudoise.core.exception.ValidationException;

import java.util.regex.Pattern;

public record CompanyIdentifier(String value) {
    public static Pattern P = Pattern.compile("^[A-Za-z]{3}-[0-9]{3}$");
    public CompanyIdentifier {
        if (value == null || !P.matcher(value).matches()) throw new ValidationException("invalid companyIdentifier");
    }
}
