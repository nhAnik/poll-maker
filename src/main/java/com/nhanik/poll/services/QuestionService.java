package com.nhanik.poll.services;

import com.nhanik.poll.exception.ResourceNotFoundException;
import com.nhanik.poll.models.Choice;
import com.nhanik.poll.models.Question;
import com.nhanik.poll.models.User;
import com.nhanik.poll.payload.QuestionRequest;
import com.nhanik.poll.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;
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

    public List<Question> getAllPollQuestions() {
        return questionRepository.findAll();
    }

    public Question getPollQuestion(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", id));
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
