package de.semesterprojekt.quiz.database.controller;

import de.semesterprojekt.quiz.config.GameConfig;
import de.semesterprojekt.quiz.database.entity.PlayedGame;
import de.semesterprojekt.quiz.database.entity.User;
import de.semesterprojekt.quiz.database.model.HighscoreEntry;
import de.semesterprojekt.quiz.database.model.UserScore;
import de.semesterprojekt.quiz.database.repository.PlayedGameRepository;
import de.semesterprojekt.quiz.database.repository.UserRepository;
import de.semesterprojekt.quiz.database.utility.HighscoreEntryComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The class controls the REST-mapping for the PlayedGames-entity
 */
@CrossOrigin
@RestController
public class PlayedGameController {

    private PlayedGameRepository playedGameRepository;

    @Autowired
    private UserRepository userRepository;

    public PlayedGameController(PlayedGameRepository playedGameRepository) {
        this.playedGameRepository = playedGameRepository;
    }

    /**
     * TODO: CREATE PRIVATE INDEX FUNCTION
     * TODO: CREATE HIGHSCORE LIST
     * CHECK FOR HIGHSCORE
     */

    /**
     * index method that returns all played games
     * @return
     */
    private List<PlayedGame> index() {
        return playedGameRepository.findAll();
    }

    /**
     * Returns the highscore list
     */
    @GetMapping(path = "/highscore")
    public ResponseEntity<List<HighscoreEntry>> getHighScore() {

        //Get all played games
        List<PlayedGame> playedGameList = index();
        List<HighscoreEntry> highscoreList = new ArrayList<>();

        if(playedGameList.size() > 0) {

            //Create and add two HighscoreEntry-objects per PlayedGame
            for(PlayedGame playedGame : playedGameList) {
                highscoreList.add(new HighscoreEntry(playedGame.getTimeStamp(), playedGame.getUser1(), playedGame.getUserScore1()));
                highscoreList.add(new HighscoreEntry(playedGame.getTimeStamp(), playedGame.getUser2(), playedGame.getUserScore2()));
            }

            //Sort the highscoreList
            Collections.sort(highscoreList, new HighscoreEntryComparator());

            //Shrink the list to the maximum size of LENGTH_HIGHSCORE_LIST
            if(highscoreList.size() > GameConfig.LENGTH_HIGHSCORE_LIST) {
                highscoreList = highscoreList.subList(0, GameConfig.LENGTH_HIGHSCORE_LIST);
            }

            //Return the List
            return ResponseEntity.ok(highscoreList);
        }

        //No played games found, return a bad request
        return ResponseEntity.badRequest().build();
    }

    /**
     * Returns a list of all played games of the calling user
     */
    @GetMapping( path = "/playedGames")
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
                for(PlayedGame playedGame : playedGameRepository.findAll()) {

                    if(playedGame.getUser1().getUserId() == userId) {

                        //Check the first player in the playedGame
                        userGameList.add(new UserScore(playedGame.getTimeStamp(), playedGame.getUserScore1(), playedGame.getUserScore2(), playedGame.getUser2()));

                    } else if (playedGame.getUser2().getUserId() == userId) {

                        //Check the second player in the playedGame
                        userGameList.add(new UserScore(playedGame.getTimeStamp(), playedGame.getUserScore2(), playedGame.getUserScore1(), playedGame.getUser1()));

                    }
                }



                //TODO: SORT LIST

                //Return the list of games
                return ResponseEntity.ok(userGameList);
            }
        }

        //User/No played games found, return a bad request
        return ResponseEntity.badRequest().build();
    }
}
