package de.semesterprojekt.quiz.data.model;

import de.semesterprojekt.quiz.data.entity.User;
import lombok.Data;

import java.util.Date;

/**
 * The class represents a user's and opponent's score
 */
@Data
public class UserScore {

    public UserScore (Date timeStamp, int userScore, int opponentScore, User opponent) {
        this.timeStamp = timeStamp;
        this.userScore = userScore;
        this.opponentScore = opponentScore;
        this.opponent = opponent.getSimpleUser();
    }

    private Date timeStamp;
    private int userScore;
    private int opponentScore;
    private SimpleUser opponent;
}