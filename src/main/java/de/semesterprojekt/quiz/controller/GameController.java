package de.semesterprojekt.quiz.controller;

import de.semesterprojekt.quiz.entity.Game;
import de.semesterprojekt.quiz.entity.User;
import de.semesterprojekt.quiz.factory.GameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * The class controls the game
 */
@Component
public class GameController implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private GameFactory gamefactory;

    /**
     * The method is called when the server is loaded up
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        //TEST DATA
        User user1 = new User();
        user1.setUserName("Bernd");

        User user2 = new User();
        user2.setUserName("Beate");

        Game newGame = gamefactory.createGame(user1, user2);
        System.out.println(newGame.getQuestion(0).getQuestionText());
        System.out.println(newGame.getQuestion(1).getQuestionText());
        System.out.println(newGame.getQuestion(2).getQuestionText());
    }
}