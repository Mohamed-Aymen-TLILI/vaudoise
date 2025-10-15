package com.test.vaudoise.domain.model;

import com.test.vaudoise.core.exception.ValidationException;

import java.util.UUID;

public record ClientId(UUID value) {
    public ClientId {
        if (value == null) throw new ValidationException("name is null");
    }
}
