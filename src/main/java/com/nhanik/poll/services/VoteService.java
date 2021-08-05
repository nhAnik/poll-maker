package com.nhanik.poll.services;

import com.nhanik.poll.models.Choice;
import com.nhanik.poll.models.Question;
import com.nhanik.poll.models.User;
import com.nhanik.poll.models.Vote;
import com.nhanik.poll.payload.VoteRequest;
import com.nhanik.poll.repositories.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteService {

    private static final Logger logger = LoggerFactory.getLogger(VoteService.class);

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ChoiceService choiceService;

    @Autowired
    private VoteRepository voteRepository;

    @Transactional
    public void castVoteToPoll(Long qid, VoteRequest request, User user) {
        Long choiceId = request.getChoiceId();
        Question question = questionService.getPollQuestion(qid);
        Choice choice = choiceService.getPollChoice(choiceId);
        Vote vote = new Vote();
        vote.setUser(user);
        vote.setQuestion(question);
        vote.setChoice(choice);
        voteRepository.save(vote);
        choiceService.incrementPollVoteCount(choiceId);
    }
}
