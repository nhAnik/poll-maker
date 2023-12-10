package com.nhanik.poll.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(
        name = "vote_table",
        uniqueConstraints = @UniqueConstraint(
                name = "user_question_pair_unique",
                columnNames = {"question_id", "user_id"}
        )
)
public class Vote {
    @Id
    @SequenceGenerator(
            name = "vote_sequence_generator",
            sequenceName = "vote_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "vote_sequence_generator"
    )
    private Long voteId;

    @ManyToOne
    @JoinColumn(
            name = "question_id",
            referencedColumnName = "questionId",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_QUESTION_ID")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Question question;

    @ManyToOne
    @JoinColumn(
            name = "choice_id",
            referencedColumnName = "choiceId",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CHOICE_ID")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Choice choice;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "userId",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_ID")
    )
    private User user;

    public Vote() {}

    public Vote(Question question, Choice choice, User user) {
        this.question = question;
        this.choice = choice;
        this.user = user;
    }

    public Long getVoteId() {
        return voteId;
    }

    public void setVoteId(Long voteId) {
        this.voteId = voteId;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Choice getChoice() {
        return choice;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
