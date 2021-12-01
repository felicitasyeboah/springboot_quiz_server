package de.semesterprojekt.quiz.controller;

import de.semesterprojekt.quiz.entity.User;
import de.semesterprojekt.quiz.repository.UserRepository;
import de.semesterprojekt.quiz.security.JwtAuthenticationFilter;
import de.semesterprojekt.quiz.security.JwtTokenProvider;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @GetMapping("/readyUsers")
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
    @GetMapping("/readyUsernames")
    public List<String> getReadyUsernames(){

        List<String> usernameList = new ArrayList<>();

        for(User user : getReadyUsers()) {
            usernameList.add(user.getUserName());
        }

        return usernameList;
    }

    /**
     * Sets the ready-status of a player
     */
    @PatchMapping( path = "/isReady")
    public ResponseEntity<User> setReadyStatus(){

        String username;

        //Get username from context
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal)
                    .getUsername();
        } else {
            username = principal
                    .toString();
        }

        //Find the user, set isReady and return the user
        for(User user : index()) {

            if (user.getUserName().equals(username)) {

                //Set the new status
                user.setReady(true);

                //Save the new status to the database
                User readyUser = userRepository.save(user);

                System.out.println(username + " is ready to play.");
                return ResponseEntity.ok(readyUser);
            }
        }

        //User not found
        return ResponseEntity.badRequest().build();
    }
}