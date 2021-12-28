package de.semesterprojekt.quiz.database.controller;

import de.semesterprojekt.quiz.config.GameConfig;
import de.semesterprojekt.quiz.database.entity.PlayedGame;
import de.semesterprojekt.quiz.database.entity.User;
import de.semesterprojekt.quiz.database.model.HighscoreEntry;
import de.semesterprojekt.quiz.database.model.ScoreEntryDateComparator;
import de.semesterprojekt.quiz.database.model.ScoreEntryScoreAndDateComparator;
import de.semesterprojekt.quiz.database.model.UserScoreEntry;
import de.semesterprojekt.quiz.database.repository.PlayedGameRepository;
import de.semesterprojekt.quiz.database.repository.UserRepository;
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
import java.util.Optional;

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
     * TODO: CHECK FOR HIGHSCORE
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

            //Sort the highscoreList by score and then by date
            Collections.sort(highscoreList, new ScoreEntryScoreAndDateComparator());

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
    public ResponseEntity<List<UserScoreEntry>> getAllPlayedGames(){

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

        //Get the userId
        Optional<User> userOptional = userRepository.findByUserName(username);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            int userId = user.getUserId();

            //List for all played games of the user
            List<UserScoreEntry> userGameList = new ArrayList<>();

            //Check all played games and create UserScoreEntry objects
            for(PlayedGame playedGameList : playedGameRepository.findAll()) {

                if(playedGameList.getUser1().getUserId() == userId) {

                    //Check the first player in the playedGame
                    userGameList.add(new UserScoreEntry(playedGameList.getTimeStamp(), playedGameList.getUserScore1(), playedGameList.getUserScore2(), playedGameList.getUser2()));

                } else if (playedGameList.getUser2().getUserId() == userId) {

                    //Check the second player in the playedGame
                    userGameList.add(new UserScoreEntry(playedGameList.getTimeStamp(), playedGameList.getUserScore2(), playedGameList.getUserScore1(), playedGameList.getUser1()));
                }
            }

            //Check if there are entries in the list
            if(userGameList.size() > 0) {

                //Sort the list by date
                Collections.sort(userGameList, new ScoreEntryDateComparator());

                //Shrink the list to the maximum size of LENGTH_HIGHSCORE_LIST
                if(userGameList.size() > GameConfig.LENGTH_USER_PLAYED_GAMES_LIST) {
                    userGameList = userGameList.subList(0, GameConfig.LENGTH_USER_PLAYED_GAMES_LIST);
                }

                //Return the list
                return ResponseEntity.ok(userGameList);
            }
        }

        //No User or played games found, return a bad request
        return ResponseEntity.badRequest().build();
    }
}
