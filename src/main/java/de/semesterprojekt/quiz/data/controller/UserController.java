package de.semesterprojekt.quiz.data.controller;

import de.semesterprojekt.quiz.data.entity.User;
import de.semesterprojekt.quiz.data.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

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

        //TODO: return own user object

        return userRepository.findAll();
    }
}