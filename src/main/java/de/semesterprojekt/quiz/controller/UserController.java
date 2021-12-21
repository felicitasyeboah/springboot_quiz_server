package de.semesterprojekt.quiz.controller;

import de.semesterprojekt.quiz.entity.User;
import de.semesterprojekt.quiz.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The class controls the REST-mapping for the User-entity
 */
@CrossOrigin("http://localhost:8080")
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
    @PatchMapping( path = "/setReadyStatus")
    public ResponseEntity<User> setReadyStatus(@RequestParam boolean toggle){

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
                user.setReady(toggle);

                //Save the new status to the database
                User readyUser = userRepository.save(user);

                //Print status
                System.out.println("Set " + username + "'s ready status to " + toggle + ".");

                //check the ready player count when toggled to ready
                if(toggle){
                    checkForTwoReadyPlayers();
                }

                //Return toggled user
                return ResponseEntity.ok(readyUser);
            }
        }

        //User not found
        return ResponseEntity.badRequest().build();
    }

    /**
     * The class checks, if there are two ready players and starts a game
     */
    private void checkForTwoReadyPlayers() {

        //Create two users
        User user1 = null;
        User user2 = null;

        //Finds two online users and invokes startGame method
        for(User user : index()) {
            if(user.isReady() == true) {
                if(user1 == null) {
                    user1 = user;
                } else if(user2 == null) {
                    user2 = user;

                    //Resets the ready status
                    user1.setReady(false);
                    user2.setReady(false);
                    userRepository.save(user1);
                    userRepository.save(user2);

                    //Starts a new game
                    //TODO: write methods to start a game
                    System.out.println(user1.getUserName() + " vs. " + user2.getUserName());








                }
            }
        }
    }
}