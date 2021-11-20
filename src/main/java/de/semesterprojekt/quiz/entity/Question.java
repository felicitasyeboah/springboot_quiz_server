package de.semesterprojekt.quiz.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int questionID;

    private int categoryID;

    private String questionText;

    private String answerCorrect;

    private String answerWrong1;

    private String answerWrong2;

    private String answerWrong3;
}
