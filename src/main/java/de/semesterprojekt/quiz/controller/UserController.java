package de.semesterprojekt.quiz.controller;

import de.semesterprojekt.quiz.entity.User;
import de.semesterprojekt.quiz.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * The class controls the REST-mapping for the User-entity
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private UserRepository userRepository;

    /**
     * Constructor
     * @param userRepository
     */
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns a list of all users
     * @return All users
     */
    @GetMapping("")
    public List<User> index(){

        return userRepository.findAll();
    }

    /**
     * Returns a list of all ready users
     * @return List of ready users
     */
    @GetMapping("ready")
    public List<User> getReadyUsers(){

        List<User> userReadyList = new ArrayList<>();

        //Extracts all online users
        for(User user : index()) {
            if(user.isReady() == true) {
                userReadyList.add(user);
            }
        }

        return userReadyList;
    }

    /**
     * Returns a list of the ready usernames
     * @return List of ready usernames
     */
    @GetMapping("readyUsername")
    public List<String> getReadyUsernames(){

        List<String> usernameList = new ArrayList<>();

        for(User user : getReadyUsers()) {
            usernameList.add(user.getUserName());
        }

        return usernameList;
    }
}