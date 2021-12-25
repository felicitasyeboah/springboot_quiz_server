package de.semesterprojekt.quiz.model;

import de.semesterprojekt.quiz.entity.Question;
import de.semesterprojekt.quiz.entity.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a round of a game. It will be send to the user
 */
@Getter
public class GameMessage {

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

    //Stores the score of the user
    private int userScore;

    //Stores the score of the opponent
    private int opponentScore;

    //Store the user and opponent
    private SimpleUser user;
    private SimpleUser opponent;


    public GameMessage(User user, User opponent, int userScore, int opponentScore, Question question) {

        this.user = user.getSimpleUser();
        this.opponent = opponent.getSimpleUser();
        this.userScore = userScore;
        this.opponentScore = opponentScore;
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

        this.correctAnswer = answer.indexOf(question.getAnswerCorrect()) + 1;

        //Print the correct answer
        System.out.println("Correct Answer: answer" + this.correctAnswer);

        //Store the answers in the variables
        answer1 = answer.get(0);
        answer2 = answer.get(1);
        answer3 = answer.get(2);
        answer4 = answer.get(3);
    }
}
