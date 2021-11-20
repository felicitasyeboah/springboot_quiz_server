package de.semesterprojekt.quiz.controller;

import de.semesterprojekt.quiz.entity.User;
import de.semesterprojekt.quiz.repository.UserRepository;
import de.semesterprojekt.quiz.request.AuthRequest;
import de.semesterprojekt.quiz.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserRepository userRepository;

    // Interface zum Encoden  des PW
    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private JwtTokenProvider jwtTokenProvider;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Registerit einen neuen Nutzer in der DB
     * @param authRequest
     * @return
     */

    @PostMapping(value = "/register") // /register route
    public ResponseEntity<User> register(@RequestBody AuthRequest authRequest) {
        Optional<User> userOptional = userRepository.findByUserName((authRequest.getUserName()));

        if(userOptional.isPresent()) {
            return ResponseEntity.badRequest().build(); //User ist bereits vorhanden
        }

        User user = new User();
        user.setUserName(authRequest.getUserName());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword())); //password verschluessseln

        User created = userRepository.save(user);

        return ResponseEntity.ok(created);
    }

    @PostMapping(value = "/login") // /login route
    public ResponseEntity<String>login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUserName(),
                        authRequest.getPassword()
                )
        );
        return ResponseEntity.ok(jwtTokenProvider.generateToken(authentication));
    }
}
