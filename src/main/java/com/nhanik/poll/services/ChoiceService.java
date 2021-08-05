package com.nhanik.poll.services;

import com.nhanik.poll.models.Choice;
import com.nhanik.poll.models.Question;
import com.nhanik.poll.models.User;
import com.nhanik.poll.repositories.ChoiceRepository;
import com.nhanik.poll.repositories.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ChoiceService {

    private static final Logger logger = LoggerFactory.getLogger(ChoiceService.class);

    @Autowired
    private ChoiceRepository choiceRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private void checkUserPermission(Question question, Long uid) {
        if (question.getUser().getUserId() != uid) {
            logger.error("This user is not permitted to update");
            throw new IllegalStateException("Permission denied");
        }
    }

    public Choice createPollChoice(Long qid, Choice choice, User user) {
        Question question = findQuestionById(qid);
        checkUserPermission(question, user.getUserId());
        choice.setQuestion(question);
        return choiceRepository.save(choice);
    }

    public Choice createPollChoice(Choice choice) {
        return choiceRepository.save(choice);
    }

    public List<Choice> getPollAllChoices(Long qid) {
        return choiceRepository.findAllByQuestionId(qid);
    }

    private void checkValidQuestionId(Long quesId) {
        if (!questionRepository.existsById(quesId)) {
            throw new IllegalStateException("Question not found");
        }
    }

    public Choice getPollChoice(Long qid, Long cid) {
        checkValidQuestionId(qid);
        return getPollChoice(cid);
    }

    public Choice getPollChoice(Long cid) {
        return choiceRepository
                .findById(cid)
                .orElseThrow(() -> new IllegalStateException("Comment not found"));
    }

    @Transactional
    public void updatePollChoice(Long qid, Long cid, Choice updatedChoice, User user) {
        Question question = findQuestionById(qid);
        checkUserPermission(question, user.getUserId());
        Choice choice = updatePollChoiceText(qid, cid, updatedChoice.getChoiceText());
        choice.setVoteCount(updatedChoice.getVoteCount());
    }

    private Choice updatePollChoiceText(Long qid, Long cid, String updatedChoiceText) {
        Choice choice = getPollChoice(qid, cid);
        if (updatedChoiceText != null &&
                updatedChoiceText.length() != 0 &&
                !updatedChoiceText.equals(choice.getChoiceText())) {
            choice.setChoiceText(updatedChoiceText);
        }
        return choice;
    }

    public void incrementPollVoteCount(Long cid) {
        choiceRepository.incrementVoteCountByChoiceId(cid);
    }

    public void deletePollChoice(Long qid, Long cid, User user) {
        Question question = findQuestionById(qid);
        checkUserPermission(question, user.getUserId());
        if (!choiceRepository.existsById(cid)) {
            throw new IllegalStateException("Comment not found");
        }
        choiceRepository.deleteById(cid);
    }

    private Question findQuestionById(Long qid) {
        return questionRepository.findById(qid)
                .orElseThrow(() -> new IllegalStateException("Question not found"));
    }
}
