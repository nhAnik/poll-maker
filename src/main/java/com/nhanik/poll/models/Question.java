package com.nhanik.poll.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "userId",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_ID")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
