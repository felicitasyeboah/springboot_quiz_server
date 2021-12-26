package de.semesterprojekt.quiz.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * The class represents a question
 */
@Data
@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int questionId;

    private String questionText;

    private String answerCorrect;

    private String answerWrong1;

    private String answerWrong2;

    private String answerWrong3;

    @ManyToOne @JoinColumn(name = "category_id",nullable = false)
    @JsonIgnoreProperties("categoryId")
    Category category;
}
