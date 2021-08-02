package com.nhanik.poll.controllers;

import com.nhanik.poll.models.Question;
import com.nhanik.poll.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("polls")
    public Question createPollQuestion(@RequestBody Question question) {
        return questionService.createPollQuestion(question);
    }

    @GetMapping(path = "polls")
    public List<Question> getAllPollQuestions() {
        return questionService.getAllPollQuestions();
    }

    @GetMapping(path = "polls/{qid}")
    public Question getPollQuestion(@PathVariable("qid") Long id) {
        return questionService.getPollQuestion(id);
    }

    @PutMapping(path = "polls/{qid}")
    public void updatePollQuestion(@PathVariable("qid") Long id, @RequestBody Question question) {
        questionService.updatePollQuestion(id, question);
    }

    @DeleteMapping(path = "polls/{qid}")
    public void deletePollQuestion(@PathVariable("qid") Long id) {
        questionService.deletePollQuestion(id);
    }
}
