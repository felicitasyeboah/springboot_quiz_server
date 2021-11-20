package de.semesterprojekt.quiz.security;

import de.semesterprojekt.quiz.entity.User;
import de.semesterprojekt.quiz.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /* Holt User aus Datenbank */
    /**
     * Sucht einen User anhand seines Usernames in der DB und gibt einen neuen Springuser anhand von
     * Username, Password und Privilegien zurück
     * @param userName
     * @return Object UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException("Der Benutzer wurde nicht gefunden."));

        /* neuer Springuser wird zurueckgegeben */
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                /* keine Privilegien werden uebergeben */
                Collections.emptyList()
        );
    }
}
