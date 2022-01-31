package de.semesterprojekt.quiz.database.model;

import de.semesterprojekt.quiz.database.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * The class represents a user's and opponent's score, it adds a simple user (opponent) and an opponent score to a score entry
 */
@Getter
@Setter
public class UserScoreEntry extends ScoreEntry {

    private int opponentScore;
    private SimpleUser opponent;

    /**
     * Contructor
     * @param timeStamp timestamp
     * @param userScore score of the user
     * @param opponentScore score of the opponent
     * @param opponent opponent user object
     */
    public UserScoreEntry (Date timeStamp, int userScore, int opponentScore, User opponent) {

        super(timeStamp, userScore);
        this.opponentScore = opponentScore;
        this.opponent = opponent.getSimpleUser();
    }
}
