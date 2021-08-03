package com.nhanik.poll.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question_table")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @OneToMany(
            mappedBy = "question"
    )
    @JsonIgnoreProperties("question")
    private List<Choice> choices;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "userId",
//            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_ID")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    public Question(String questionText, User user) {
        this.questionText = questionText;
        this.user = user;
        this.choices = new ArrayList<>();
    }

    public void addChoice(Choice choice) {
        choices.add(choice);
    }
}
