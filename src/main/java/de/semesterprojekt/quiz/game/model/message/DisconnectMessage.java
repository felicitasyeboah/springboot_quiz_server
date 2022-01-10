package de.semesterprojekt.quiz.game.model.message;

/**
 * The class represents a disconnect message that will be sent when a user disconnects during a game
 */
public class DisconnectMessage extends GenericMessage{

    public DisconnectMessage() {
        super.setType(MessageType.DISCONNECT_MESSAGE);
    }
}
