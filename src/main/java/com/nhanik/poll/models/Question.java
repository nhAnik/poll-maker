package com.nhanik.poll.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question_table")
public class Question {

    @Id
    @SequenceGenerator(
            name = "question_sequence_generator",
            sequenceName = "question_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "question_sequence_generator"
    )
    private Long questionId;

    @Column(nullable = false)
    private String questionText;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @OneToMany(
            mappedBy = "question"
    )
    @JsonIgnoreProperties("question")
    private List<Choice> choices;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "userId",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_QUESTION_ID")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @Transient
    private String ownerName;

    public Question() {}

    public Question(String questionText, LocalDateTime expiresAt, User user) {
        this.questionText = questionText;
        this.expiresAt = expiresAt;
        this.user = user;
        this.choices = new ArrayList<>();
    }

    public Question(Long questionId, String questionText, LocalDateTime expiresAt, List<Choice> choices, User user) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.expiresAt = expiresAt;
        this.choices = choices;
        this.user = user;
    }

    public String getOwnerName() {
        return user.getFirstName() + " " + user.getLastName();
    }

    public void addChoice(Choice choice) {
        choices.add(choice);
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
