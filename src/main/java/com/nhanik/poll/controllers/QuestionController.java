package com.nhanik.poll.controllers;

import com.nhanik.poll.models.Question;
import com.nhanik.poll.models.User;
import com.nhanik.poll.payload.QuestionRequest;
import com.nhanik.poll.payload.UpdatedTextRequest;
import com.nhanik.poll.payload.VoteRequest;
import com.nhanik.poll.services.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    // Always create question with some choices
    @PostMapping("polls")
    public ResponseEntity<?> createPollQuestionWithChoices(
            @Valid @RequestBody QuestionRequest request,
            @AuthenticationPrincipal User user) {
        Question question = questionService.createPollQuestionWithChoices(request, user);
        return ResponseEntity.ok(question);
    }

    @PostMapping(path = "polls/{qid}/choices")
    public ResponseEntity<?> addChoiceForQuestion(
            @PathVariable("qid") Long id,
            @RequestBody UpdatedTextRequest request,
            @AuthenticationPrincipal User user) {
        Question question = questionService.addChoiceForQuestion(id, request, user);
        return ResponseEntity.ok(question);
    }

    @PostMapping(path = "polls/{qid}/votes")
    public ResponseEntity<?> castVoteToPoll(
            @PathVariable Long qid,
            @Valid @RequestBody VoteRequest request,
            @AuthenticationPrincipal User user) {
        questionService.castVoteToPoll(qid, request, user);
        return ResponseEntity.ok("Vote cast successful!");
    }

    @GetMapping(path = "polls")
    public ResponseEntity<?> getAllPollQuestions() {
        return ResponseEntity.ok(questionService.getAllPollQuestions());
    }

    @GetMapping(path = "polls/{qid}")
    public ResponseEntity<?>  getPollQuestion(@PathVariable("qid") Long id) {
        return ResponseEntity.ok(questionService.getPollQuestion(id));
    }

    @PutMapping(path = "polls/{qid}")
    public ResponseEntity<?> updatePollQuestion(
            @PathVariable("qid") Long id,
            @Valid @RequestBody UpdatedTextRequest request,
            @AuthenticationPrincipal User user) {
        Question question = questionService.updatePollQuestion(id, request, user);
        return ResponseEntity.ok(question);
    }

    @DeleteMapping(path = "polls/{qid}")
    public ResponseEntity<?> deletePollQuestion(@PathVariable("qid") Long id, @AuthenticationPrincipal User user) {
        questionService.deletePollQuestion(id, user);
        return ResponseEntity.ok("Question deleted");
    }
}
