package com.nhanik.poll.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        name = "user_table",
        uniqueConstraints = @UniqueConstraint(
                name = "email_unique",
                columnNames = {"email"}
        )
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @SequenceGenerator(
            name = "user_sequence_generator",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence_generator"
    )
    private Long userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
}
