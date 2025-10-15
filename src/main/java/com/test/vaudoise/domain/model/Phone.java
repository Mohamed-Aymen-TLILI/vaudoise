package com.test.vaudoise.domain.model;

import com.test.vaudoise.core.exception.ValidationException;

public record Phone(String value) {
    public Phone {
        if (value == null) throw new ValidationException("invalid phone");
        String normalized = value.replaceAll("[\\s.\\-()]", "");
        if (normalized.startsWith("0041")) {
            normalized = "+" + normalized.substring(2);
        }
        boolean ok = normalized.matches("^\\+41\\d{9}$") || normalized.matches("^0\\d{9}$");
        if (!ok) throw new ValidationException("invalid phone");
        value = normalized;
    }

}
