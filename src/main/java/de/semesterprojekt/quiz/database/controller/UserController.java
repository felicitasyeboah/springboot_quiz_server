package de.semesterprojekt.quiz.database.controller;

import de.semesterprojekt.quiz.database.entity.User;
import de.semesterprojekt.quiz.database.repository.UserRepository;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/**
 * The class controls the REST-mapping for the User-entity
 */
@CrossOrigin("http://localhost:8080")
@RestController
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
     * Returns the user object of the calling user
     * @return All users
     */
    @GetMapping(path = "/user")
    public User index(){

        //Get the user from the securityContext
        return getUserFromSecurityContext(SecurityContextHolder.getContext());
    }

    /**
     * The method returns the user by the username
     */
    public User getUserFromUserName(String userName) {

        //Get the user
        Optional<User> userOptional = userRepository.findByUserName(userName);
        if(userOptional.isPresent()) {
            return userOptional.get();
        }

        //No user found
        return null;
    }

    /**
     * The method returns the user of the securityContext
     * @param securityContext SecurityContext
     * @return User
     */
    public User getUserFromSecurityContext(SecurityContext securityContext) {

        String userName;

        //Get principal from context
        Object principal = securityContext
                .getAuthentication()
                .getPrincipal();

        //Set the username
        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal)
                    .getUsername();
        } else {
            userName = principal
                    .toString();
        }

        //Return the user
        return getUserFromUserName(userName);
    }
}