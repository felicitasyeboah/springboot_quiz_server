package de.semesterprojekt.quiz.game.model.message;

import de.semesterprojekt.quiz.database.entity.User;
import de.semesterprojekt.quiz.database.model.SimpleUser;
import lombok.Getter;

/**
 * The class represents a result message that will be sent once at the end of the game
 */
@Getter
public class ResultMessage extends ScoreMessage {

    //Add a value that represents, that a user achieved a new highscore
    private boolean isHighScore;

    //The winner of the game
    private SimpleUser winner;

    public ResultMessage(User user, User opponent, User winner, int userScore, int opponentScore, boolean isHighScore) {

        //construct the extended class
        super(user, opponent, userScore, opponentScore);

        //Set the type
        super.setType(MessageType.RESULT_MESSAGE);

        //Set the isHighscore value
        this.isHighScore = isHighScore;

        //Set the winner
        if(winner != null) {
            this.winner = winner.getSimpleUser();
        } else {
            this.winner = null;
        }
    }
}
