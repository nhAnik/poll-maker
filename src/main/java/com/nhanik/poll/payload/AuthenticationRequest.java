package com.nhanik.poll.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationRequest (
        @NotBlank String email,
        @NotBlank @Size(min = 8, max = 15) String password
) {}
