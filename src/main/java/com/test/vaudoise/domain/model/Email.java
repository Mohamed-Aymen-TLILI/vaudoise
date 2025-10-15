package com.test.vaudoise.domain.model;

import com.test.vaudoise.core.exception.ValidationException;

import java.util.regex.Pattern;

public record Email(String value) {
    private static final Pattern P = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    public Email {
        if (value == null || !P.matcher(value).matches()) {
            throw new ValidationException("Invalid email address");
        }
    }
}
