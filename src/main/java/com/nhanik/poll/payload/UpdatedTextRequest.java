package com.nhanik.poll.payload;

import jakarta.validation.constraints.NotBlank;

public record UpdatedTextRequest(@NotBlank String updatedText) {}
