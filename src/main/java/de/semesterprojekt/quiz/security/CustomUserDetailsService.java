package de.semesterprojekt.quiz.security;

import de.semesterprojekt.quiz.entity.User;
import de.semesterprojekt.quiz.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * The class...
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    /**
     * Constructor
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /* Holt User aus Datenbank */
    /**
     * Loads a user based on the userName attribute from the database and returns a new spring user with username, password and privileges.
     * @param userName username
     * @return Object UserDetails
     * @throws UsernameNotFoundException User not found
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        //search for user or throw an exception
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException("Username could not be found."));

        //returns a new spring user
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),

                //no privileges
                Collections.emptyList()
        );
    }
}