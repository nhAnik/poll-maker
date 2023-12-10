package com.nhanik.poll.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "choice_table")
public class Choice {
    @Id
    @SequenceGenerator(
            name = "choice_sequence_generator",
            sequenceName = "choice_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "choice_sequence_generator"
    )
    private Long choiceId;

    @Column(nullable = false)
    private String choiceText;

    private Integer voteCount = 0;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "question_id",
            referencedColumnName = "questionId",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_QUESTION_ID")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Question question;

    public Choice(String choiceText, Question question) {
        this.choiceText = choiceText;
        this.question = question;
    }

    public Choice() {}

    public Choice(Long choiceId, String choiceText, Integer voteCount, Question question) {
        this.choiceId = choiceId;
        this.choiceText = choiceText;
        this.voteCount = voteCount;
        this.question = question;
    }

    public Long getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(Long choiceId) {
        this.choiceId = choiceId;
    }

    public String getChoiceText() {
        return choiceText;
    }

    public void setChoiceText(String choiceText) {
        this.choiceText = choiceText;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
