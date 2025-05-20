package com.nhanik.poll.payload;

import com.nhanik.poll.validators.ValidEmail;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record RegistrationRequest (
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotBlank @ValidEmail String email,
    @NotBlank @Size(min = 8, max = 15) String password
) {}
