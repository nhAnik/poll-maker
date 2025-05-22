package com.nhanik.poll.payload;

import java.util.List;

public record PollResultResponse(
        Long questionId,
        String questionText,
        List<ChoiceResult> results,
        long totalVotes
) {}

