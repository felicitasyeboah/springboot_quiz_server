package de.semesterprojekt.quiz;

import de.semesterprojekt.quiz.entity.User;
import de.semesterprojekt.quiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OnStartUp implements ApplicationListener<ContextRefreshedEvent> {

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