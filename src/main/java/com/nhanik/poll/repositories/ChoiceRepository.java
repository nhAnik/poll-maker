package com.nhanik.poll.repositories;

import com.nhanik.poll.models.Choice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChoiceRepository extends JpaRepository<Choice, Long> {

    @Query("from Choice c where c.question.questionId = ?1")
    List<Choice> findAllByQuestionId(Long qid);

    @Modifying
    @Query("update Choice c set c.voteCount = c.voteCount + 1 where c.choiceId = ?1")
    void incrementVoteCountByChoiceId(Long cid);
}
