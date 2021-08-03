package com.nhanik.poll.services;

import com.nhanik.poll.models.Choice;
import com.nhanik.poll.models.Question;
import com.nhanik.poll.models.User;
import com.nhanik.poll.payload.QuestionRequest;
import com.nhanik.poll.repositories.ChoiceRepository;
import com.nhanik.poll.repositories.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.security.Principal;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ChoiceService choiceService;

    @Autowired
    private UserService userService;

    public Question createPollQuestion(Question question) {
        return questionRepository.save(question);
    }

    private void checkMissingChoices(QuestionRequest request) {
        if (request.getNumOfChoices() <= 0) {
            throw new IllegalStateException("Choices are missing");
        }
    }

    private void checkUserPermission(Question question, Long uid) {
        if (question.getUser().getUserId() != uid) {
            logger.error("This user is not permitted to update");
            throw new IllegalStateException("Permission denied");
        }
    }

    @Transactional
    public Question createPollQuestionWithChoices(QuestionRequest request, User user) {
        checkMissingChoices(request);
        Question question = questionRepository.save(new Question(request.getQuestionText(), user));
        request.getChoices()
                .forEach(choiceText -> {
                    Choice choice = choiceService.createPollChoice(new Choice(choiceText, question));
                    question.addChoice(choice);
                });
        return question;
    }

    public List<Question> getAllPollQuestions() {
        return questionRepository.findAll();
    }

    public Question getPollQuestion(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Poll question with id " + id + " does not exist"));
    }

    @Transactional
    public Question updatePollQuestion(Long qid, Question updateQuestion, User user) {
        Question question = getPollQuestion(qid);

        // Check if the user is same one who crated the question
        checkUserPermission(question, user.getUserId());

        String updatedQuesText = updateQuestion.getQuestionText();
        question.setQuestionText(updatedQuesText);
        return question;
    }

    public void deletePollQuestion(Long qid, User user) {
        Question question = getPollQuestion(qid);
        checkUserPermission(question, user.getUserId());
        questionRepository.deleteById(qid);
    }


}
