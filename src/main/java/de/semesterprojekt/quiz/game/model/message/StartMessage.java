package de.semesterprojekt.quiz.game.model.message;

import de.semesterprojekt.quiz.database.entity.User;
import de.semesterprojekt.quiz.database.model.SimpleUser;
import lombok.Getter;

/**
 * The class represents a start message that will be sent once at the start of the game and contains the opponent
 */
@Getter
public class StartMessage extends GenericMessage {

    //The opponent of the game
    private SimpleUser opponent;

    public StartMessage(User opponent) {



        //construct the extended class
        super(MessageType.START_MESSAGE);

        //Set the opponent
        if(opponent != null) {
            this.opponent = opponent.getSimpleUser();
        } else {
            this.opponent = null;
        }
    }
}
