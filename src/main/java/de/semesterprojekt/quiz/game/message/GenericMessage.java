package de.semesterprojekt.quiz.game.message;

import lombok.Data;

@Data
public class GenericMessage {

    private MessageType type;

    public GenericMessage() {
        this.type = MessageType.GENERIC_MESSAGE;
    }

}
