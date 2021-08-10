package com.nhanik.poll.controllers;

import com.nhanik.poll.models.Choice;
import com.nhanik.poll.models.User;
import com.nhanik.poll.services.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChoiceController {

    private final QuestionService questionService;

    @PutMapping(path = "polls/{qid}/choices/{cid}")
    public ResponseEntity<?> updatePollChoice(
            @PathVariable("qid") Long qid,
            @PathVariable("cid") Long cid,
            @RequestBody Choice choice,
            @AuthenticationPrincipal User user) {
        Choice updatedChoice = questionService.updateChoiceForQuestion(qid, cid, choice, user);
        return ResponseEntity.ok(updatedChoice);
    }

    @DeleteMapping(path = "polls/{qid}/choices/{cid}")
    public ResponseEntity<?> deletePollChoice(
            @PathVariable("qid") Long qid,
            @PathVariable("cid") Long cid,
            @AuthenticationPrincipal User user) {
        questionService.deletePollChoiceForQuestion(qid, cid, user);
        return ResponseEntity.ok("Choice deleted");
    }
}
