package de.semesterprojekt.quiz.game.model.message;

import lombok.Data;

/**
 * The class represents a generic message
 */
@Data
public class GenericMessage {

    private MessageType type;

    public GenericMessage(MessageType messageType) {

        //Set the type
        this.type = messageType;
    }
}
