package com.nhanik.poll.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record RegistrationRequest (
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotBlank String email,
    @NotBlank @Size(min = 8, max = 15) String password
) {}
