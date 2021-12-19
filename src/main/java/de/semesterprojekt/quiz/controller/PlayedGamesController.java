package de.semesterprojekt.quiz.controller;

import de.semesterprojekt.quiz.entity.PlayedGames;
import de.semesterprojekt.quiz.entity.User;
import de.semesterprojekt.quiz.entity.UserScore;
import de.semesterprojekt.quiz.repository.PlayedGamesRepository;
import de.semesterprojekt.quiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The class controls the REST-mapping for the PlayedGames-entity
 */
@CrossOrigin
@RestController
@RequestMapping("/playedGames")
public class PlayedGamesController {

    private PlayedGamesRepository playedGamesRepository;

    @Autowired
    private UserRepository userRepository;

    public PlayedGamesController(PlayedGamesRepository playedGamesRepository) {
        this.playedGamesRepository = playedGamesRepository;
    }

    /**
     * Returns a list of all played games
     * @return
     */
    @GetMapping("")
    public List<PlayedGames> index(){

        return playedGamesRepository.findAll();
    }

    /**
     * Returns a list of all played games of the calling user
     */
    /**
     * Sets the ready-status of a player
     */
    @GetMapping( path = "/getAllPlayedGames")
    public ResponseEntity<List<UserScore>> getAllPlayedGames(){

        String username;

        //Get username from context
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        //Set the username
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal)
                    .getUsername();
        } else {
            username = principal
                    .toString();
        }

        //Find the user and get its id
        for(User user : userRepository.findAll()) {

            if (user.getUserName().equals(username)) {

                //Set the user id
                int userId = user.getUserId();

                //List for all played games of the user
                List<UserScore> userGameList = new ArrayList<>();

                //Check all played games and create UserScore objects
                for(PlayedGames playedGame : index()) {

                    if(playedGame.getUser1().getUserId() == userId) {

                        //Check the first player in the playedGame
                        userGameList.add(new UserScore(playedGame.getTimeStamp(), playedGame.getUserScore1(), playedGame.getUserScore2(), playedGame.getUser2()));

                    } else if (playedGame.getUser2().getUserId() == userId) {

                        //Check the second player in the playedGame
                        userGameList.add(new UserScore(playedGame.getTimeStamp(), playedGame.getUserScore2(), playedGame.getUserScore1(), playedGame.getUser1()));

                    }
                }

                //Return the list of games
                return ResponseEntity.ok(userGameList);
            }
        }

        //User/No played games found
        return ResponseEntity.badRequest().build();
    }
}
