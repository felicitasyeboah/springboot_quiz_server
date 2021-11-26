package de.semesterprojekt.quiz.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int questionId;

    //private int categoryId;

    private String questionText;

    private String answerCorrect;

    private String answerWrong1;

    private String answerWrong2;

    private String answerWrong3;

    @ManyToOne @JoinColumn(name = "category_id",nullable = false)
    Category category;
}
