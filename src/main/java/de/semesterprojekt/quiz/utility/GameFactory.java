package de.semesterprojekt.quiz.utility;

import de.semesterprojekt.quiz.model.Game;
import de.semesterprojekt.quiz.entity.Question;
import de.semesterprojekt.quiz.entity.User;
import de.semesterprojekt.quiz.repository.QuestionRepository;
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

    @Autowired
    QuestionRepository questionRepository;

    /**
     * The method creates a Game session object
     * @param user1 User 1
     * @param user2 User 2
     * @return Game session
     */
    public Game createGame(User user1, String tokenUser1, User user2, String tokenUser2) {

        Game newGame = new Game(user1, tokenUser1, user2, tokenUser2, questionRandomizer.getQuestions(questionCount));

        return newGame;
    }
}