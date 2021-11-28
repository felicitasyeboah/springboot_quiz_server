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
    public List<User> Index(){

        return userRepository.findAll();
    }

    /**
     * Returns a list of all online users
     * @return List of online users
     */
    @GetMapping("online")
    public List<User> getOnlineUsers(){

        List<User> userOnlineList = new ArrayList<>();

        //Extracts all online users
        for(User user : Index()) {
            if(user.isOnline() == true) {
                userOnlineList.add(user);
            }
        }

        return userOnlineList;
    }

    /**
     * Returns a list of the online usernames
     * @return List of online usernames
     */
    @GetMapping("onlineUsername")
    public List<String> getOnlineUsernames(){

        List<String> usernameList = new ArrayList<>();

        for(User user : getOnlineUsers()) {
            usernameList.add(user.getUserName());
        }

        return usernameList;
    }
}