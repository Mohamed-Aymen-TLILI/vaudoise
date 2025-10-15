package com.test.vaudoise.domain.model;


import com.test.vaudoise.core.exception.ValidationException;

public record Name(String value) {
    public Name {
        if (value == null) throw new ValidationException("name required");
        var valueLength = value.trim().length();
        if (valueLength < 2 || valueLength > 200 ) throw new ValidationException("name length must be between 2 and 200 characters");
    }
}
