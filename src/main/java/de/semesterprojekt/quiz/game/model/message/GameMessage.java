package de.semesterprojekt.quiz.game.model.message;

import de.semesterprojekt.quiz.database.entity.Question;
import de.semesterprojekt.quiz.database.entity.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a round of a game. It will be sent to the user before each round
 */
@Getter
public class GameMessage extends ScoreMessage {

    //Stores the category
    private String category;

    //Stores the question
    private String question;

    //Stores 4 answers in random order
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;

    //the index of the correct answer '1' -> answer1, '2' -> answer2,...
    private int correctAnswer;

    //number of total rounds
    private int totalRounds;

    //number of current round
    private int currentRound;

    public GameMessage(User user, User opponent, int userScore, int opponentScore, Question question, int currentRound, int totalRounds) {

        //construct the extended class
        super(user, opponent, userScore, opponentScore);

        //Set the message type
        super.setType(MessageType.GAME_MESSAGE);

        //Set the question and category
        this.question = question.getQuestionText();
        this.category = question.getCategory().getCategoryName();

        //Add the answers to a Collection
        List<String> answer = new ArrayList<>();
        answer.add(question.getAnswerCorrect());
        answer.add(question.getAnswerWrong1());
        answer.add(question.getAnswerWrong2());
        answer.add(question.getAnswerWrong3());

        //Shuffle the answers
        Collections.shuffle(answer);

        //Set the index for the correct answer
        this.correctAnswer = answer.indexOf(question.getAnswerCorrect()) + 1;

        //Store the answers in the variables
        answer1 = answer.get(0);
        answer2 = answer.get(1);
        answer3 = answer.get(2);
        answer4 = answer.get(3);

        //Set the round attributes
        this.totalRounds = totalRounds;
        this.currentRound = currentRound;
    }
}