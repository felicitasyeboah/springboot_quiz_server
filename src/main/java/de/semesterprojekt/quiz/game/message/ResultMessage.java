package de.semesterprojekt.quiz.game.message;

import de.semesterprojekt.quiz.database.entity.User;
import lombok.Getter;

/**
 * The class represents a result message that will be sent once at the end of the game
 */
@Getter
public class ResultMessage extends ScoreMessage {

    private boolean isHighScore;

    public ResultMessage(User user, User opponent, int userScore, int opponentScore, boolean isHighScore) {
        super(user.getSimpleUser(), opponent.getSimpleUser(), userScore, opponentScore);
        this.isHighScore = isHighScore;
        super.setType(MessageType.RESULT_MESSAGE);
    }
}
