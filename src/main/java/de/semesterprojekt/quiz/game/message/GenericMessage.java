package de.semesterprojekt.quiz.game.message;

import lombok.Data;

/**
 * The class represents a generic message
 */
@Data
public class GenericMessage {

    private MessageType type;

    public GenericMessage() {

        //Set the type
        this.type = MessageType.GENERIC_MESSAGE;
    }
}
