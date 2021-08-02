package com.nhanik.poll.services;

import com.nhanik.poll.models.Question;
import com.nhanik.poll.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public Question createPollQuestion(Question question) {
        return questionRepository.save(question);
    }

    public List<Question> getAllPollQuestions() {
        return questionRepository.findAll();
    }

    public Question getPollQuestion(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Poll question with id " + id + " does not exist"));
    }

    @Transactional
    public void updatePollQuestion(Long id, Question updatedQuestion) {
        Question question = getPollQuestion(id);
        String updatedQuesText = updatedQuestion.getQuestionText();

        if (updatedQuesText== null ||
                updatedQuesText.length() == 0 ||
                updatedQuesText.equals(question.getQuestionText()))
            return;

        question.setQuestionText(updatedQuesText);
    }

    public void deletePollQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new IllegalStateException("Poll question with id " + id + " does not exist");
        }
        questionRepository.deleteById(id);
    }
}
