package de.semesterprojekt.quiz.controller;

import de.semesterprojekt.quiz.entity.User;
import de.semesterprojekt.quiz.repository.UserRepository;
import de.semesterprojekt.quiz.request.AuthRequest;
import de.semesterprojekt.quiz.security.JwtTokenProvider;
import netscape.javascript.JSObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The class controls the register and login process
 */

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserRepository userRepository;

    //Interface for encoding the password
    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private JwtTokenProvider jwtTokenProvider;

    /**
     * AuthContructor
     *
     * @param userRepository
     * @param passwordEncoder
     * @param authenticationManager
     * @param jwtTokenProvider
     */
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Registers a new user to the system
     *
     * @param authRequest request
     * @return
     */
    //register route

    @PostMapping(value = "/register")
    public ResponseEntity<User> register(@RequestBody AuthRequest authRequest) {
        Optional<User> userOptional = userRepository.findByUserName((authRequest.getUserName()));


        //return bad request when username is not available
        if (userOptional.isPresent()) {
            System.out.println("Username \"" + authRequest.getUserName() + "\" is occupied.");
            return ResponseEntity.badRequest().build();
        }


        //create new user
        User newUser = new User();
        newUser.setUserName(authRequest.getUserName());
        newUser.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        newUser.setProfileImage("irgendeinbild");
        //save new user to database
        User createdUser = userRepository.save(newUser);

        //return created user object
        System.out.println("Username \"" + authRequest.getUserName() + "\" has been created.");
        return ResponseEntity.ok(createdUser);
    }


    /**
     * Logs in a user
     *
     * @param authRequest request
     * @return JW-Token
     */
    //login route

    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest authRequest) {

        //creates an authentication
        Authentication authentication = null;

        try {
            authentication = authenticationManager.authenticate(

                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUserName(),
                            authRequest.getPassword()
                    )
            );

            //returns JW-Token when user is authenticated
            System.out.println("User \"" + authRequest.getUserName() + "\" successfully logged in.");
            Map<String, String> token = new HashMap<>();
            token.put("token", jwtTokenProvider.generateToken(authentication));
            return ResponseEntity.ok(token);

        } catch (Exception exception) {

            //returns bad request otherwise
            System.out.println("User \"" + authRequest.getUserName() + "\" failed to log in.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();//badRequest().build();
        }
    }
}
