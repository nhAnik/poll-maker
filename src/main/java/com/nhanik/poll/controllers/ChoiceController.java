package com.nhanik.poll.controllers;

import com.nhanik.poll.models.Choice;
import com.nhanik.poll.models.User;
import com.nhanik.poll.services.ChoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChoiceController {

    @Autowired
    private ChoiceService choiceService;

    @PostMapping(path = "polls/{qid}/choices")
    public Choice createPollChoice(
            @PathVariable("qid") Long id,
            @RequestBody Choice choice,
            @AuthenticationPrincipal User user) {
        return choiceService.createPollChoice(id, choice, user);
    }

    @GetMapping(path = "polls/{qid}/choices")
    public List<Choice> getPollAllChoices(@PathVariable("qid") Long qid) {
        return choiceService.getPollAllChoices(qid);
    }

    @GetMapping(path = "polls/{qid}/choices/{cid}")
    public Choice getPollChoice(@PathVariable("qid") Long qid, @PathVariable("cid") Long cid) {
        return choiceService.getPollChoice(qid, cid);
    }

    @PutMapping(path = "polls/{qid}/choices/{cid}")
    public void updatePollChoice(
            @PathVariable("qid") Long qid,
            @PathVariable("cid") Long cid,
            @RequestBody Choice choice,
            @AuthenticationPrincipal User user) {
        choiceService.updatePollChoice(qid, cid, choice, user);
    }

    @DeleteMapping(path = "polls/{qid}/choices/{cid}")
    public void deletePollChoice(
            @PathVariable("qid") Long qid,
            @PathVariable("cid") Long cid,
            @AuthenticationPrincipal User user) {
        choiceService.deletePollChoice(qid, cid, user);
    }
}
