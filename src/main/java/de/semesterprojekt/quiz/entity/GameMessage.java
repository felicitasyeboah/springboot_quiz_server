package de.semesterprojekt.quiz.entity;

import de.semesterprojekt.quiz.repository.CategoryRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a round of a game. It will be send to the user
 */
@Getter
public class GameMessage {

    public GameMessage(User user, User opponent, int userScore, int opponentScore, Question question) {
        this.user = user;
        this.opponent = opponent;
        this.userScore = userScore;
        this.opponentScore = opponentScore;
        this.question = question.getQuestionText();
        this.category = question.getCategory().getCategoryName();

        //Add the answers
        this.answer = new ArrayList<>();
        this.answer.add(question.getAnswerCorrect());
        this.answer.add(question.getAnswerWrong1());
        this.answer.add(question.getAnswerWrong2());
        this.answer.add(question.getAnswerWrong3());

        //Shuffle the answers
        Collections.shuffle(this.answer);
    }

    //Stores the category
    private String category;

    //Stores the question
    private String question;

    //Stores 4 answers in random order
    private List<String> answer;

    //Stores the score of the user
    private int userScore;

    //Stores the score of the opponent
    private int opponentScore;

    //Stores the user
    private User user;

    //Stores the opponent
    private User opponent;
}
