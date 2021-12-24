package de.semesterprojekt.quiz.controller;

import de.semesterprojekt.quiz.model.Game;
import de.semesterprojekt.quiz.model.GameMessage;
import de.semesterprojekt.quiz.entity.User;
import de.semesterprojekt.quiz.repository.UserRepository;
import de.semesterprojekt.quiz.utility.GameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The class controls the game
 */
@Component
public class GameController implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private GameFactory gamefactory;

    @Autowired
    UserRepository userRepository;

    /**
     * The method is called when the server is loaded up
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        //Set every user's isReady-status to false
        List<User> userList = userRepository.findAll();
        for(User user : userList) {
            if(user.isReady() == true) {
                user.setReady(false);
                userRepository.save(user);
            }
        }
    }
}