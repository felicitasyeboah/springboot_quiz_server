package de.semesterprojekt.quiz.model;

import de.semesterprojekt.quiz.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * The class represents a user's and opponent's score
 */
@Data
@AllArgsConstructor
public class UserScore {

    private Date timeStamp;
    private int userScore;
    private int opponentScore;
    private User opponent;
}
