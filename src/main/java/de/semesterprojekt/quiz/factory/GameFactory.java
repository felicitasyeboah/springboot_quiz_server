package de.semesterprojekt.quiz.factory;

import de.semesterprojekt.quiz.utility.QuestionRandomizer;
import de.semesterprojekt.quiz.entity.Game;
import de.semesterprojekt.quiz.entity.Question;
import de.semesterprojekt.quiz.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The class is a factory for a Game session object
 */
@Component
public class GameFactory {

    private int questionCount = 3;

    @Autowired
    private QuestionRandomizer questionRandomizer;

    /**
     * The method creates a Game session object
     * @param user1 User 1
     * @param user2 User 2
     * @return Game session
     */
    public Game createGame(User user1, User user2) {

        Game newGame = new Game(user1, user2, questionCount);

        //Defines the minimum number of questions to 1
        if(questionCount < 1) {
            questionCount = 1;
        }

        Question[] questionList = questionRandomizer.getQuestions(questionCount);
        newGame.setQuestions(questionList);

        return newGame;
    }
}