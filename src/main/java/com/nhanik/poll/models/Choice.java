package com.nhanik.poll.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "choice_table")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @ManyToOne
    @JoinColumn(
            name = "question_id",
            referencedColumnName = "questionId",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_QUESTION_ID")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Question question;
}
