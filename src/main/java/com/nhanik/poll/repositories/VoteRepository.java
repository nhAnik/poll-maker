package com.nhanik.poll.repositories;

import com.nhanik.poll.models.Question;
import com.nhanik.poll.models.User;
import com.nhanik.poll.models.Vote;
import com.nhanik.poll.payload.ChoiceResult;
import com.nhanik.poll.payload.PollResultResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    boolean existsByQuestionAndUser(Question question, User user);

    @Query("""
        SELECT new  com.nhanik.poll.payload.ChoiceResult(
            c.choiceId,
            c.choiceText,
            COUNT(v)
        )
        FROM Vote v
        JOIN v.choice c
        WHERE v.question.questionId = :questionId
        GROUP BY c.choiceId, c.choiceText
        """)
    List<ChoiceResult> countVotesByChoiceForQuestion(Long questionId);
}
