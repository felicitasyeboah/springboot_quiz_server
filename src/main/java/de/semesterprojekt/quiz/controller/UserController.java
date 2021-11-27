package de.semesterprojekt.quiz.controller;

import de.semesterprojekt.quiz.entity.User;
import de.semesterprojekt.quiz.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The class controls the REST-mapping for the User-entity
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Returns a list of all Users
    @GetMapping("")
    public List<User> Index(){
        return userRepository.findAll();
    }
}
