package com.nhanik.poll.payload;

import jakarta.validation.constraints.Min;

public record VoteRequest(@Min(0) Long choiceId) {}
