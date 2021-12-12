package de.semesterprojekt.quiz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * The class represents a user's and enemy's score
 */
@Data
@AllArgsConstructor
public class UserScore {


    private Date timeStamp;
    private int userScore;
    private int enemyScore;
    private User enemy;
}
