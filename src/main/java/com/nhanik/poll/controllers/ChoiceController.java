package com.nhanik.poll.controllers;

import com.nhanik.poll.models.Choice;
import com.nhanik.poll.models.User;
import com.nhanik.poll.payload.UpdatedTextRequest;
import com.nhanik.poll.services.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChoiceController {

    private final QuestionService questionService;

    public ChoiceController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PutMapping(path = "polls/{qid}/choices/{cid}")
    public ResponseEntity<?> updatePollChoice(
            @PathVariable("qid") Long qid,
            @PathVariable("cid") Long cid,
            @RequestBody UpdatedTextRequest request,
            @AuthenticationPrincipal User user) {
        Choice updatedChoice = questionService.updateChoiceForQuestion(qid, cid, request, user);
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
