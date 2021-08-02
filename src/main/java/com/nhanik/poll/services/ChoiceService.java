package com.nhanik.poll.services;

import com.nhanik.poll.models.Choice;
import com.nhanik.poll.repositories.ChoiceRepository;
import com.nhanik.poll.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ChoiceService {

    @Autowired
    private ChoiceRepository choiceRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public Choice createPollChoice(Long qid, Choice choice) {
        return questionRepository
                .findById(qid)
                .map(question -> {
                    choice.setQuestion(question);
                    return choiceRepository.save(choice);
                })
                .orElseThrow(() -> new IllegalStateException("Question not found"));
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
        return choiceRepository
                .findById(cid)
                .orElseThrow(() -> new IllegalStateException("Comment not found"));
    }

    @Transactional
    public void updatePollChoice(Long qid, Long cid, Choice updatedChoice) {
        checkValidQuestionId(qid);

        Choice choice = getPollChoice(qid, cid);
        String updatedChoiceText = updatedChoice.getChoiceText();
        if (updatedChoiceText != null &&
                updatedChoiceText.length() != 0 &&
                !updatedChoiceText.equals(choice.getChoiceText())) {
            choice.setChoiceText(updatedChoiceText);
        }

        choice.setVoteCount(updatedChoice.getVoteCount());
    }

    public void deletePollChoice(Long qid, Long cid) {
        checkValidQuestionId(qid);
        if (!choiceRepository.existsById(cid)) {
            throw new IllegalStateException("Comment not found");
        }
        choiceRepository.deleteById(cid);
    }
}
