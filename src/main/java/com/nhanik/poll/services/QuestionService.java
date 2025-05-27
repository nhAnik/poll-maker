package com.nhanik.poll.services;

import com.nhanik.poll.exception.ChoiceRemoveFailureException;
import com.nhanik.poll.exception.ExpiredPollException;
import com.nhanik.poll.exception.InvalidVoteException;
import com.nhanik.poll.exception.ResourceNotFoundException;
import com.nhanik.poll.models.Choice;
import com.nhanik.poll.models.Question;
import com.nhanik.poll.models.User;
import com.nhanik.poll.models.Vote;
import com.nhanik.poll.payload.*;
import com.nhanik.poll.repositories.QuestionRepository;
import com.nhanik.poll.repositories.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;
    private final VoteRepository voteRepository;
    private final ChoiceService choiceService;

    public QuestionService(QuestionRepository questionRepository, VoteRepository voteRepository, ChoiceService choiceService) {
        this.questionRepository = questionRepository;
        this.voteRepository = voteRepository;
        this.choiceService = choiceService;
    }

    private void checkUserPermission(Question question, Long uid) {
        if (question.getUser().getUserId() != uid) {
            logger.error("This user is not permitted to update");
            throw new IllegalStateException("Permission denied");
        }
    }

    private void checkExpiry(Question question) {
        if (question.getExpiresAt().isBefore(LocalDateTime.now())) {
            logger.error("Poll with id {} has been expired", question.getQuestionId());
            throw new ExpiredPollException(question.getQuestionId());
        }
    }

    @Transactional
    public Question createPollQuestionWithChoices(QuestionRequest request, User user) {
        if (request.expiresAt().isBefore(LocalDateTime.now())) {
            throw new ExpiredPollException("Poll cannot be created with old date time.");
        }
        Question question = questionRepository.save(new Question(request.questionText(), request.expiresAt(), user));
        request.choices()
                .forEach(choiceText -> {
                    Choice choice = choiceService.createPollChoice(new Choice(choiceText, question));
                    question.addChoice(choice);
                });
        return question;
    }

    public Question addChoiceForQuestion(Long qid, UpdatedTextRequest request, User user) {
        Question question = getPollQuestion(qid);
        checkUserPermission(question, user.getUserId());
        checkExpiry(question);
        choiceService.createPollChoice(new Choice(request.updatedText(), question));
        return question;
    }

    @Transactional
    public void castVoteToPoll(Long qid, VoteRequest request, User user) {
        Long choiceId = request.choiceId();
        Question question = getPollQuestion(qid);
        checkExpiry(question);
        Choice choice = choiceService.getPollChoice(choiceId);
        if (!Objects.equals(choice.getQuestion().getQuestionId(), qid)) {
            throw new InvalidVoteException("Choice does not belong to the given poll");
        }
        voteRepository
                .findByQuestionAndUser(question, user)
                .ifPresent(vote -> {
                    throw new InvalidVoteException("User has already cast a vote in this question");
                });

        voteRepository.save(new Vote(question, choice, user));
        choiceService.incrementPollVoteCount(choiceId);
    }

    public List<Question> getAllPollQuestions() {
        return questionRepository.findAll();
    }

    public Question getPollQuestion(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", id));
    }

    public PollResultResponse getPollResult(Long id) {
        Question question = getPollQuestion(id);
        List<ChoiceResult> choiceResults = voteRepository
                .countVotesByChoiceForQuestion(question.getQuestionId());
        long voteCount = choiceResults.stream()
                .map(ChoiceResult::getVoteCount)
                .reduce(0L, Long::sum);
        return new PollResultResponse(
                question.getQuestionId(),
                question.getQuestionText(),
                choiceResults,
                voteCount
        );
    }

    @Transactional
    public Question updatePollQuestion(Long qid, UpdatedTextRequest request, User user) {
        Question question = getPollQuestion(qid);
        checkUserPermission(question, user.getUserId());
        checkExpiry(question);
        question.setQuestionText(request.updatedText());
        return question;
    }

    public void deletePollQuestion(Long qid, User user) {
        Question question = getPollQuestion(qid);
        checkUserPermission(question, user.getUserId());
        questionRepository.deleteById(qid);
    }

    @Transactional
    public Choice updateChoiceForQuestion(Long qid, Long cid, UpdatedTextRequest request, User user) {
        Question question = getPollQuestion(qid);
        checkUserPermission(question, user.getUserId());
        checkExpiry(question);
        return choiceService.updatePollChoice(cid, request);
    }

    public List<Choice> getPollAllChoices(Long qid) {
        Question question = getPollQuestion(qid);
        return question.getChoices();
    }

    public void deletePollChoiceForQuestion(Long qid, Long cid, User user) {
        Question question = getPollQuestion(qid);
        checkUserPermission(question, user.getUserId());
        if (question.getChoices().size() <= 2) {
            throw new ChoiceRemoveFailureException(qid);
        }
        choiceService.deletePollChoice(cid);
    }
}
