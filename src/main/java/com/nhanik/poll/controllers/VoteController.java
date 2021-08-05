package com.nhanik.poll.controllers;

import com.nhanik.poll.models.User;
import com.nhanik.poll.payload.VoteRequest;
import com.nhanik.poll.services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping("polls/{qid}/votes")
    public ResponseEntity<?> castVoteToPoll(
            @PathVariable Long qid,
            @Valid @RequestBody VoteRequest request,
            @AuthenticationPrincipal User user) {
        voteService.castVoteToPoll(qid, request, user);
        return ResponseEntity.ok("Vote cast successful!");
    }
}
