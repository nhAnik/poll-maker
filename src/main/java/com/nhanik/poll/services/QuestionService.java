package com.nhanik.poll.services;

import com.nhanik.poll.exception.ChoiceRemoveFailureException;
import com.nhanik.poll.exception.MultipleVoteException;
import com.nhanik.poll.exception.ResourceNotFoundException;
import com.nhanik.poll.models.Choice;
import com.nhanik.poll.models.Question;
import com.nhanik.poll.models.User;
import com.nhanik.poll.models.Vote;
import com.nhanik.poll.payload.QuestionRequest;
import com.nhanik.poll.payload.UpdatedTextRequest;
import com.nhanik.poll.payload.VoteRequest;
import com.nhanik.poll.repositories.QuestionRepository;
import com.nhanik.poll.repositories.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;
    private final VoteRepository voteRepository;
    private final ChoiceService choiceService;

    private void checkUserPermission(Question question, Long uid) {
        if (question.getUser().getUserId() != uid) {
            logger.error("This user is not permitted to update");
            throw new IllegalStateException("Permission denied");
        }
    }

    @Transactional
    public Question createPollQuestionWithChoices(QuestionRequest request, User user) {
        Question question = questionRepository.save(new Question(request.getQuestionText(), user));
        request.getChoices()
                .forEach(choiceText -> {
                    Choice choice = choiceService.createPollChoice(new Choice(choiceText, question));
                    question.addChoice(choice);
                });
        return question;
    }

    public Question addChoiceForQuestion(Long qid, UpdatedTextRequest request, User user) {
        Question question = getPollQuestion(qid);
        checkUserPermission(question, user.getUserId());
        choiceService.createPollChoice(new Choice(request.getUpdatedText(), question));
        return question;
    }

    @Transactional
    public void castVoteToPoll(Long qid, VoteRequest request, User user) {
        Long choiceId = request.getChoiceId();
        Question question = getPollQuestion(qid);
        Choice choice = choiceService.getPollChoice(choiceId);
        voteRepository
                .findByQuestionAndUser(question, user)
                .ifPresent(vote -> {
                    throw new MultipleVoteException();
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

    @Transactional
    public Question updatePollQuestion(Long qid, UpdatedTextRequest request, User user) {
        Question question = getPollQuestion(qid);
        checkUserPermission(question, user.getUserId());
        question.setQuestionText(request.getUpdatedText());
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
