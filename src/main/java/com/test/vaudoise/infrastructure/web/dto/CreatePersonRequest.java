package com.test.vaudoise.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreatePersonRequest(
        @NotBlank(message = "name is required")
        String name,
        @NotBlank(message = "email is required")
        String email,
        @NotBlank(message = "phone is required")
        String phone,
        @NotNull(message = "Birthdate is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate birthdate
) {}
