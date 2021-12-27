package de.semesterprojekt.quiz.game.model.message;

import de.semesterprojekt.quiz.data.entity.User;
import lombok.Getter;

/**
 * The class represents a result message that will be sent once at the end of the game
 */
@Getter
public class ResultMessage extends ScoreMessage {

    //Add a value that represents, that a user achieved a new highscore
    private boolean isHighScore;

    public ResultMessage(User user, User opponent, int userScore, int opponentScore, boolean isHighScore) {

        //construct the extended class
        super(user, opponent, userScore, opponentScore);

        //Set the type
        super.setType(MessageType.RESULT_MESSAGE);

        //Set the value
        this.isHighScore = isHighScore;
    }
}
